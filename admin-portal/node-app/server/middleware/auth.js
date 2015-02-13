exports.apiAuth = function(req, res, next) {
  var request = require('request');
  var utils = require('./../commons/utils');
  var host = utils.get_request_host(req);
  var options = {
    url: 'http://' + host + '/wp-json/auth',
    headers: {
      'Cookie': req.headers.cookie
    }
  };
  request(options, function(error, response, body) {
    if (error) {
      res.statusCode = 500;
      next(error);
    }
    switch (response.statusCode) {
      case 200:
        next();
        break;
      case 403:
        res.statusCode = 403;
        var msg = 'Not authorized';
        res.send(msg);
        return next(new Error(msg));
        break;
      default:
        res.statusCode = response.statusCode;
        return next(new Error(body));
    }

  });
};