package br.com.vvedana.imarket;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class ProdutoAdapter extends ArrayAdapter<Produto>{

    public ProdutoAdapter(Context context, List<Produto> lista) {
        super(context, 0, lista);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_produtos, parent, false);
        }

        Produto prod = getItem(position);

        TextView produto = (TextView) itemView.findViewById(R.id.txtProduto);
        produto.setText(prod.getNome());

        TextView valor = (TextView) itemView.findViewById(R.id.txtValor);
        valor.setText(String.valueOf(prod.getPrecoMin()));

        ImageView img = (ImageView) itemView.findViewById(R.id.imgView);
        new DownloadImageTask(img).execute(prod.getUrl().toString());
        return itemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

