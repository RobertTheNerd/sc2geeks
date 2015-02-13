/**
 * Created by robert on 10/6/14.
 */
exports.get_start_index = function(total, pageSize, pageNo) {
  var start = (pageNo - 1) * pageSize;
  if (start < 0 || start >= total) {
    start = 0;
  }

  return start;
}

exports.get_request_host = function(req) {
  if ('x-forwarded-host' in req.headers) {
    return req.headers['x-forwarded-host'];
  }
  return req.get('host');
};