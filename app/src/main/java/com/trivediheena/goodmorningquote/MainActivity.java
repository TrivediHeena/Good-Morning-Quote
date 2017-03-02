package com.trivediheena.goodmorningquote;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {

    TextView tv1;
    RelativeLayout rel;
    final static String str1="http://api.theysaidso.com/qod.xml";
    static InputStream is=null;String ex="",data="";

    StringBuffer sb=new StringBuffer("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tv1=(TextView)findViewById(R.id.txtHello);
        rel=(RelativeLayout)findViewById(R.id.rel);

        new MethodEx(this).execute();
			/*obj=new HandleXML(str1);
			obj.fetchXML();
			while(obj.parsingComplete);
			obj.getText()+"\n"+obj.getAuthor()*/
        StringBuilder url=new StringBuilder(str1);
        String fullUrl=url.toString();
        try{
            URL web=new URL(str1);
    }
        catch(Exception e){
            tv1.setText("Exception: Can't Fetch Data");
            e.printStackTrace();
        }
    }

    public void forward_click(View v) {
        Intent intent=new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        //intent.setPackage("com.whatsapp");
        intent.putExtra(Intent.EXTRA_TEXT, tv1.getText());
        startActivity(Intent.createChooser(intent, "Share With"));
    }
    public void save_click(View v){
        try{
            DBHelper h=new DBHelper(this);
            h.open();
            h.createEntry(tv1.getText().toString());
            h.close();
            Toast.makeText(getApplicationContext(), "Your Quote Saved Successfully!!", Toast.LENGTH_LONG).show();
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }
    public void list_click(View v){
        startActivity(new Intent(getApplicationContext(), Select.class));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
      /*  if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    class MethodEx extends AsyncTask<String, Void, String> {

        private ProgressDialog progressDialog;
        private Context context;
        //private TextView txtRes;
        public MethodEx(Context context){
            this.context=context;
            //this.txtRes=txtRes;
        }
        public String getInternetData(String url) throws Exception{
            BufferedReader br=null;
            String data=null;
            try{
                HttpClient client=new DefaultHttpClient();
                //URI web=new URI("http://quotes.rationalmind.net/random.php?format=xml");
                HttpGet req=new HttpGet(url);
                //req.setURI(web);
                HttpResponse res=client.execute(req);
                HttpEntity entity=res.getEntity();
                data= EntityUtils.toString(entity);
				/*br=new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
				String l="";String nl=System.getProperty("line.separator");
				StringBuffer sb=new StringBuffer("");
				while((l=br.readLine())!=null){
					sb.append(l+nl);
				}*/
                //br.close();
                //data=sb.toString();
                //return data;
            }
            catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
                    try {
                        //String str="http://quotes.rationalmind.net/random.php?format=xml";
                        String str = "http://api.theysaidso.com/qod.xml";
                        URL url = new URL(str);
                        HttpClient client = new DefaultHttpClient();
                        HttpGet request = new HttpGet();
                        request.setURI(new URI(str));
                        HttpResponse response = client.execute(request);
                        SAXParserFactory factory = SAXParserFactory.newInstance();
                        SAXParser sp = factory.newSAXParser();
                        XMLReader xr = sp.getXMLReader();
                        DefaultHandler handler = new DefaultHandler() {
                            boolean text = false;
                            boolean auth = false;

                            @Override
                            public void characters(char[] ch, int start, int length)
                                    throws SAXException {
                                // TODO Auto-generated method stub
                                //super.characters(ch, start, length);
                                if (text) {
                                    sb.append("Morning Quote\n");
                                    sb.append(new String(ch, start, length)).append("\n-");
                                    //tv1.setText(new String(ch,start,length));
                                    text = false;
                                }
                                if (auth) {
                                    sb.append(new String(ch, start, length));
                                    //tv1.setText(new String(ch,start,length));
                                    auth = false;
                                }
                            }

                            @Override
                            public void endElement(String uri, String localName,
                                                   String qName) throws SAXException {
                                // TODO Auto-generated method stub
                                super.endElement(uri, localName, qName);
                            }

                            @Override
                            public void startElement(String uri, String localName,
                                                     String qName, Attributes attributes)
                                    throws SAXException {
                                // TODO Auto-generated method stub
                                //super.startElement(uri, localName, qName, attributes);
                                if (qName.equalsIgnoreCase("quote")) {
                                    text = true;
                                }
                                if (qName.equalsIgnoreCase("author")) {
                                    auth = true;
                                }
                            }
                        };
                        InputStream is = url.openStream();
                        //InputStreamReader isr=new InputStreamReader(is, "iso-8859-1");
                        //BufferedReader br=new BufferedReader(isr);
                        sp.parse(is, handler);
                        //xr.setContentHandler(handler);
                        //sp.parse(new InputSource(br), handler);
                        //xr.parse(new InputSource(br));
                        return sb.toString();
                        //return tv1.getText().toString();
                        // tv1.setText(sb.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        return new String("Exception:Can't Fetch Data");
                    }

        }
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            tv1.setText(result);
        }
    }
}
