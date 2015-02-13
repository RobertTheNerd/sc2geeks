package poc;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.ContentEncodingHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: robert
 * Date: 12/10/12
 * Time: 11:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class Worker
{
	public static void main(String[] args) throws IOException
	{
		// testHttp();
	}

	private static void testHttp() throws IOException
	{
		HttpClient httpclient = new ContentEncodingHttpClient();
		HttpGet httpget = new HttpGet("http://www.newegg.com/");
		httpget.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		httpget.setHeader("Accept-Encoding", "gzip,deflate");

		// Execute the request
		HttpResponse response = httpclient.execute(httpget);

		// Examine the response status
		System.out.println(response.getStatusLine());

		// Get hold of the response entity
		HttpEntity entity = response.getEntity();

		// If the response does not enclose an entity, there is no need
		// to worry about connection release
		if (entity != null) {
			InputStream instream = entity.getContent();
			try {

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(instream));
				String line;
				while ((line = reader.readLine()) != null)
				// do something useful with the response
					System.out.println(line);

			} catch (IOException ex) {

				// In case of an IOException the connection will be released
				// back to the connection manager automatically
				throw ex;

			} catch (RuntimeException ex) {

				// In case of an unexpected exception you may want to abort
				// the HTTP request in order to shut down the underlying
				// connection and release it back to the connection manager.
				httpget.abort();
				throw ex;

			} finally {

				// Closing the input stream will trigger connection release
				instream.close();

			}

			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}

	}

}
