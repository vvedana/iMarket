package br.com.vvedana.imarket;

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

/**
 * Created by User on 02/07/2017.
 */

public class CarrinhoAdapter extends ArrayAdapter<Carrinho> {

    public CarrinhoAdapter(Context context, List<Carrinho> lista) {
        super(context, 0, lista);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.view_carrinho, parent, false);
        }

        Carrinho prod = getItem(position);

        TextView produto = (TextView) itemView.findViewById(R.id.txtProdutoCarr);
        produto.setText(prod.getNome());

        TextView valor = (TextView) itemView.findViewById(R.id.txtValorCarr);
        valor.setText(String.valueOf(prod.getValor()));

        TextView qtde = (TextView) itemView.findViewById(R.id.txtQtde);
        qtde.setText(String.valueOf(prod.getQtde()));

        ImageView img = (ImageView) itemView.findViewById(R.id.imgViewCarr);
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
