package com.izv.dam.newquip.vistas.notas;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.util.UtilFecha;
import com.squareup.picasso.Picasso;

import org.bouncycastle.jce.provider.BrokenPBE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Array;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.getDataDirectory;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista {

    private EditText editTextTitulo, editTextNota;
    private Nota nota = new Nota();
    private PresentadorNota presentador;
    private ImageView imagen;

    //Variables para la camara
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY_CAPTURE = 2;

    //Variables pdf

    private static final String NOMBRE_DOCUMENTO= "prueba.pdf";
    private static final String NOMBRE_DIRECTORIO= "PDF/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        Toolbar toolbar2= (Toolbar) findViewById(R.id.toolbar2) ;
        setSupportActionBar(toolbar2);


        presentador = new PresentadorNota(this);

        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);
        imagen = (ImageView) findViewById(R.id.imageView);

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
        } else {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable("nota");
            }
        }

        mostrarNota(nota);

    }


    @Override
    protected void onPause() {
        saveNota();
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
    }

    @Override
    public void mostrarNota(Nota n) {
        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getCuerpo());
        Picasso.with(this).load(Uri.parse(n.getImagen())).into(imagen);

    }

    private void saveNota() {

        nota.setTitulo(editTextTitulo.getText().toString());
        nota.setCuerpo(editTextNota.getText().toString());

        long r = presentador.onSaveNota(nota);
        if (r > 0 & nota.getId() == 0) {
            nota.setId(r);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate( R.menu.menu2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {

            case R.id.fotos: {


                if(Permisos.solicitarPermisos(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},VistaNota.this)){
                    System.out.println("Hola");
                    final String[] items = new String[]{"Camara", "Galeria", "Cancelar"};

                    AlertDialog.Builder dialog = new AlertDialog.Builder(VistaNota.this);
                    dialog.setTitle("Opciones");
                    dialog.setItems(items, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String opcion = items[which];

                            switch (opcion) {

                                case "Camara": {

                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                    }

                                    break;
                                }

                                case "Galeria": {

                                    Intent galeria = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                    startActivityForResult(galeria, REQUEST_GALLERY_CAPTURE);

                                    break;
                                }

                                case "Cancelar": {

                                    dialog.dismiss();
                                    break;
                                }
                            }

                        }
                    });

                    dialog.create().show();

                    return true;
                }

            }

            case R.id.pdf: {



                if(Permisos.solicitarPermisos(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},VistaNota.this)){



                    //Creamos el documento
                    //Document documento = new Document();

                    //Creamos el fichero
                    //File fichero = null;

                    new AsyncTask<Void, Void, Boolean>(){
                        @Override
                        protected Boolean doInBackground(Void... params) {

                            Document documento   = new Document();
                            File fichero        = null;

                            try {
                                fichero = crearFichero(NOMBRE_DOCUMENTO);
                                FileOutputStream ficheroPdf = new FileOutputStream(fichero.getAbsolutePath());
                                PdfWriter.getInstance(documento, ficheroPdf);

                                documento.open();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }


                            try {

                                String titulo= nota.getTitulo();
                                String cuerpo= nota.getCuerpo();
                                String ima=nota.getImagen();

                                com.itextpdf.text.Image image= com.itextpdf.text.Image.getInstance(ima);

                                //Centrar la imagen
                                image.setAlignment(Element.ALIGN_CENTER);


                                //Añadir al

                                if(!titulo.isEmpty() && !cuerpo.isEmpty() && !ima.isEmpty()){
                                    documento.add(new Paragraph (nota.getTitulo()));
                                    documento.add(new Paragraph(nota.getCuerpo()));
                                    documento.add(image);
                                    documento.close();
                                }

                                if(!titulo.isEmpty() && !cuerpo.isEmpty()){
                                    documento.add(new Paragraph (nota.getTitulo()));
                                    documento.add(new Paragraph(nota.getCuerpo()));
                                    documento.close();
                                }

                                if(!titulo.isEmpty()){
                                    documento.add(new Paragraph (nota.getTitulo()));
                                    documento.close();
                                }

                                if(!titulo.isEmpty() &&  !ima.isEmpty()){
                                    documento.add(new Paragraph (nota.getTitulo()));
                                    documento.add(image);
                                }




                            } catch (DocumentException e) {
                                e.printStackTrace();
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            if (documento != null) {
                                return true;

                            }
                            //  documento.close();
                            return false;

                        }

                        @Override
                        protected void onPostExecute(Boolean aBoolean) {

                            if ( aBoolean ) {

                                Toast probar = Toast.makeText(VistaNota.this, "PDG GENERADO", Toast.LENGTH_SHORT);
                                probar.show();
                            }
                        }
                    }.execute();



                } //fin if

            }//fin case
        } // fin switch
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_IMAGE_CAPTURE: {

                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");

                    //Creamos un AsynTask para ejecutar en segundo plano
                    new AsyncTask<Bitmap, Void, Uri>(){

                        @Override
                        protected Uri doInBackground(Bitmap... params) {

                            Bitmap imageBitmap = params[0];


                            //Debemos de guardar la imagen
                            String directorio = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fotos";
                            //String directorio = getFilesDir().getAbsolutePath() + File.separator + "fotos";

                            File file_dir = new File(directorio);

                            if (!file_dir.exists()) file_dir.mkdirs();

                            String nombre_foto = UtilFecha.formatDate(Calendar.getInstance().getTime()) + "_foto.jpg";
                            File archivo_foto = new File(directorio + File.separator + nombre_foto);

                            //Creamos el fichero donde guardamos la imagen
                            try {
                                archivo_foto.createNewFile();

                                FileOutputStream output = new FileOutputStream(archivo_foto);
                                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);

                                output.flush();
                                output.close();
                            } catch (IOException e) {
                            }

                            Uri uri_imagen = Uri.fromFile(archivo_foto);

                            return uri_imagen;

                        }

                        @Override
                        protected void onPostExecute(Uri uri_imagen) {

                            Picasso.with(VistaNota.this).load(uri_imagen).into(imagen);
                            nota.setImagen(uri_imagen.toString());

                        }

                    }.execute(imageBitmap);


                    break;
                }

                case REQUEST_GALLERY_CAPTURE: {

                    Uri selectedImage = data.getData();


                    new AsyncTask<Uri, Void, Uri>() {
                        @Override
                        protected Uri doInBackground(Uri... params) {

                            Uri selectedImage= params[0];

                            try {
                                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                                if (bmp != null) {

                                    String nombre_archivo = selectedImage.getLastPathSegment();

                                    String directorio = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fotos";
                                    File file_dir = new File(directorio);

                                    if (!file_dir.exists()) file_dir.mkdirs();

                                    String ruta_imagen = directorio + File.separator + nombre_archivo;
                                    File archivo_imagen = new File(ruta_imagen);

                                    if (!archivo_imagen.exists()) archivo_imagen.createNewFile();

                                    //Guardamos
                                    FileOutputStream output = new FileOutputStream(archivo_imagen);
                                    bmp.compress(Bitmap.CompressFormat.JPEG, 50, output);

                                    output.flush();
                                    output.close();

                                    //Uri uri_imagen = Uri.fromFile(archivo_imagen);
                                    selectedImage= Uri.fromFile(archivo_imagen);
                                   //return uri_imagen;
                                }

                            } catch (IOException e) {
                            }
                            return selectedImage;
                        }

                    @Override
                    protected void onPostExecute(Uri uri_imagen) {

                        nota.setImagen(uri_imagen.toString());
                        Picasso.with(VistaNota.this).load(uri_imagen).into(imagen);

                    }

                }.execute(selectedImage);
                    break;
                }

            }
        }

    }

    //Creamos el fichero del pdf
    public File crearFichero(String nombreFichero) throws  IOException{

        File ruta=getRuta();
        File fichero=null;



        if(ruta!=null){
            fichero= new File(ruta, UtilFecha.formatDate(Calendar.getInstance().getTime())+ nombreFichero);
        }

        return fichero;


    }

    //Definimos la ruta donde se guardara el pdf
    public File getRuta(){
        File ruta=null;

        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){

            ruta= new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),NOMBRE_DIRECTORIO);

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }



} // fin class





