import webapp2
import os
import jinja2
import datetime
import json
import decimal
import requests
from functools import wraps


template_dir = os.path.join(os.path.dirname(__file__),'templates')
jinja_env = jinja2.Environment(loader=jinja2.FileSystemLoader(template_dir),autoescape = True)

try:
    import urlparse
except ImportError:
    #py3k
    from urllib import parse as urlparse



__all__ = ['FirebaseAuthentication', 'FirebaseApplication']

def http_connection(timeout):
    """
    Decorator function that injects a requests.Session instance into
    the decorated function's actual parameters if not given.
    """
    def wrapper(f):
        def wrapped(*args, **kwargs):
            if not ('connection' in kwargs) or not kwargs['connection']:
                connection = requests.Session()
                kwargs['connection'] = connection
            else:
                connection = kwargs['connection']
            connection.timeout = timeout
            connection.headers.update({'Content-type': 'application/json'})
            return f(*args, **kwargs)
        return wraps(f)(wrapped)
    return wrapper

class JSONEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, (datetime.datetime, datetime.date)):
            return obj.isoformat()
        elif isinstance(obj, datetime.timedelta):
            return total_seconds(obj)
        elif isinstance(obj, decimal.Decimal):
            return float(obj)
        else:
            return json.JSONEncoder.default(self, obj)


@http_connection(60)
def make_get_request(url, params, headers, connection):
    """
    Helper function that makes an HTTP GET request to the given firebase
    endpoint. Timeout is 60 seconds.
    `url`: The full URL of the firebase endpoint (DSN appended.)
    `params`: Python dict that is appended to the URL like a querystring.
    `headers`: Python dict. HTTP request headers.
    `connection`: Predefined HTTP connection instance. If not given, it
    is supplied by the `decorators.http_connection` function.

    The returning value is a Python dict deserialized by the JSON decoder. However,
    if the status code is not 2x or 403, an requests.HTTPError is raised.

    connection = connection_pool.get_available_connection()
    response = make_get_request('http://firebase.localhost/users', {'print': silent'},
                                {'X_FIREBASE_SOMETHING': 'Hi'}, connection)
    response => {'1': 'John Doe', '2': 'Jane Doe'}
    """
    timeout = getattr(connection, 'timeout')
    response = connection.get(url, params=params, headers=headers, timeout=timeout)
    if response.ok or response.status_code == 403:
        return response.json() if response.content else None
    else:
        response.raise_for_status()


@http_connection(60)
def make_put_request(url, data, params, headers, connection):
    """
    Helper function that makes an HTTP PUT request to the given firebase
    endpoint. Timeout is 60 seconds.
    `url`: The full URL of the firebase endpoint (DSN appended.)
    `data`: JSON serializable dict that will be stored in the remote storage.
    `params`: Python dict that is appended to the URL like a querystring.
    `headers`: Python dict. HTTP request headers.
    `connection`: Predefined HTTP connection instance. If not given, it
    is supplied by the `decorators.http_connection` function.

    The returning value is a Python dict deserialized by the JSON decoder. However,
    if the status code is not 2x or 403, an requests.HTTPError is raised.

    connection = connection_pool.get_available_connection()
    response = make_put_request('http://firebase.localhost/users',
                                '{"1": "Ozgur Vatansever"}',
                                {'X_FIREBASE_SOMETHING': 'Hi'}, connection)
    response => {'1': 'Ozgur Vatansever'} or {'error': 'Permission denied.'}
    """
    timeout = getattr(connection, 'timeout')
    response = connection.put(url, data=data, params=params, headers=headers,
                              timeout=timeout)
    if response.ok or response.status_code == 403:
        return response.json() if response.content else None
    else:
        response.raise_for_status()




class FirebaseApplication(object):
    """
    Class that actually connects with the Firebase backend via HTTP calls.
    It fully implements the RESTful specifications defined by Firebase. Data
    is transmitted as in JSON format in both ways. This class needs a DSN value
    that defines the base URL of the backend, and if needed, authentication
    credentials are accepted and then are taken into consideration while
    constructing HTTP requests.

    There are also the corresponding asynchronous versions of each HTTP method.
    The async calls make use of the on-demand process pool defined under the
    module `async`.

    auth = FirebaseAuthentication(FIREBASE_SECRET, 'firebase@firebase.com', 'fbpw')
    firebase = FirebaseApplication('https://firebase.localhost', auth)

    That's all there is. Then you start connecting with the backend:

    json_dict = firebase.get('/users', '1', {'print': 'pretty'})
    print json_dict
    {'1': 'John Doe', '2': 'Jane Doe', ...}

    Async version is:
    firebase.get('/users', '1', {'print': 'pretty'}, callback=log_json_dict)

    The callback method is fed with the returning response.
    """
    NAME_EXTENSION = '.json'
    URL_SEPERATOR = '/'

    def __init__(self, dsn, authentication=None):
        assert dsn.startswith('https://'), 'DSN must be a secure URL'
        self.dsn = dsn
        self.authentication = authentication

    def _build_endpoint_url(self, url, name=None):
        """
        Method that constructs a full url with the given url and the
        snapshot name.

        Example:
        full_url = _build_endpoint_url('/users', '1')
        full_url => 'http://firebase.localhost/users/1.json'
        """
        if not url.endswith(self.URL_SEPERATOR):
            url = url + self.URL_SEPERATOR
        if name is None:
            name = ''
        return '%s%s%s' % (urlparse.urljoin(self.dsn, url), name,
                           self.NAME_EXTENSION)

    def _authenticate(self, params, headers):
        """
        Method that simply adjusts authentication credentials for the
        request.
        `params` is the querystring of the request.
        `headers` is the header of the request.

        If auth instance is not provided to this class, this method simply
        returns without doing anything.
        """
        if self.authentication:
            user = self.authentication.get_user()
            params.update({'auth': user.firebase_auth_token})
            headers.update(self.authentication.authenticator.HEADERS)

    @http_connection(60)
    def get(self, url, name, params=None, headers=None, connection=None):
        """
        Synchronous GET request.
        """
        if name is None: name = ''
        params = params or {}
        headers = headers or {}
        endpoint = self._build_endpoint_url(url, name)
        self._authenticate(params, headers)
        return make_get_request(endpoint, params, headers, connection=connection)


    @http_connection(60)
    def put(self, url, name, data, params=None, headers=None, connection=None):
        """
        Synchronous PUT request. There will be no returning output from
        the server, because the request will be made with ``silent``
        parameter. ``data`` must be a JSONable value.
        """
        assert name, 'Snapshot name must be specified'
        params = params or {}
        headers = headers or {}
        endpoint = self._build_endpoint_url(url, name)
        self._authenticate(params, headers)
        data = json.dumps(data, cls=JSONEncoder)
        return make_put_request(endpoint, data, params, headers,
                                connection=connection)


class Handler(webapp2.RequestHandler):
    def render_str(self,template,**params):
        t = jinja_env.get_template(template)
        return t.render(params)
    def render (self, template, **kw):
        self.response.out.write(self.render_str(template, **kw))
        
class Form(Handler):
    def get (self):       
        self.render("form.html")

    def post(self):
        pusername = self.request.get('uname')
        page = self.request.get('age')
        pgender = self.request.get('gender')
        pfood = self.request.get('food')
        psleep = self.request.get('sleep')
        pprof = self.request.get('profession')
        pprofhappy = self.request.get('prof_happy')
        pprofwork = self.request.get('prof_work')
        panger = self.request.get('anger')
        pcry = self.request.get('cry')
        ptrauma = self.request.get('trauma')
        plonely = self.request.get('lonely')
        pchemical = self.request.get('chemical')
        prelief = self.request.get_all('relief')
        peffect = self.request.get('effect')
        flag = False

        if (("1" in prelief) or (int(peffect)<5)):
            flag = True
        tag = ""
        fb = FirebaseApplication('https://feelgood-58fe5.firebaseio.com', None)
        path = "relax_responses/"

##      To add age group to the tag (1:14-18, 2:18-24, 3:24-30, 4:30-40, 5-40+)
        if(page == "1"):
            tag = tag+"1"
            path = path+"14_18/"
        elif(page == "2"):
            tag = tag+"2"
            path = path+"18_24/"
        elif(page == "3"):
            tag = tag+"3"
            path = path+"24_30/"
        elif(page == "4"):
            tag = tag+"4"
            path = path+"30_40/"
        elif(page == "5"):
            tag = tag+"5"
            path = path+"40/"

##      To add gender to the tag (m:male, f:female, o:other)
        if(pgender == "M"):
            tag = tag+"_M"
            path = path+"male/"
        elif(pgender == "F"):
            tag = tag+"_F"
            path = path+"female/"
        elif(pgender == "O"):
            tag = tag+"_O"
            path = path+"other/"

##      To add nutritional stress tag, if a person specifies that he doesn't have proper meals
        if(pfood == "N"):
            tag = tag+"_N"
            if (flag == False):
                for i in prelief:
##                    prev = int(fb.get(path+"nutrition/"+i,'').encode('utf-8'))
                    prev = int(fb.get(path+"nutrition/"+i,''))
                    fb.put(path,"nutrition/"+i,prev-1)

##      To add physical stress tag
        if(psleep!="2" or pprofwork == "Y"):
            tag = tag+"_P"
            if (flag == False):
                for i in prelief:
                    prev = int(fb.get(path+"physical/"+i,''))
                    fb.put(path,"physical/"+i,prev-1)

##      To add emotional stress tag
        if(ptrauma == "Y" or plonely == "3"):
            tag = tag+"_E"
            if (flag == False):
                for i in prelief:
                    prev = int(fb.get(path+"emotional/"+i,''))
                    fb.put(path,"emotional/"+i,prev-1)

##      To add anger management tag
        if(panger == "3"):
            tag = tag+"_A"

##      To add grief management tag
        if(pcry == "3"):
            tag = tag+"_G"

##      To add chemical stress tag
        if(pchemical == "Y"):
            tag = tag+"_C"
            if (flag == False):
                for i in prelief:
                    prev = int(fb.get(path+"chemical/"+i,''))
                    fb.put(path,"chemical/"+i,prev-1)

##      To add mental stress tag
        if(pprofhappy == "1" or pprofhappy == "2"):
            tag = tag+"_M"
            if (flag == False):
                for i in prelief:
                    prev = int(fb.get(path+"mental/"+i,''))
                    fb.put(path,"mental/"+i,prev-1)
       

        fb.put("users/"+pusername,"userTag",tag)
        
        self.render("thankyou.html")
        
      
app = webapp2.WSGIApplication([('/', Form)],debug=True)
