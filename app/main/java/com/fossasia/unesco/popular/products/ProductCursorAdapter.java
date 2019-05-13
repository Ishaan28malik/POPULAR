package com.fossasia.unesco.popular.products;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fossasia.unesco.popular.Preferences;
import com.fossasia.unesco.popular.R;
import com.fossasia.unesco.popular.products.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter {
    private Preferences preferences;
    private SQLiteDatabase dashboardDatabase;

    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_products, parent, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        ImageView imageView = view.findViewById(R.id.list_item_image_view);
        TextView nameTextView = view.findViewById(R.id.name_text_view);
        TextView priceTextView = view.findViewById(R.id.price_text_view);
        ImageView infoButton = view.findViewById(R.id.info_button);

        //final int _ID = cursor.getInt(cursor.getColumnIndex(AuthEntry._ID));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(ProductContract.ProductEntry.PRODUCT_IMAGE));
        final Bitmap productImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        final String productName = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.PRODUCT_NAME));
        final String productDescription = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.PRODUCT_DESCRIPTION));
        final String productPrice = cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.PRODUCT_PRICE));

        imageView.setImageBitmap(productImage);
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showApplyAlertDialog(view, context, productName, productDescription);
            }
        });

    }

    private void showApplyAlertDialog(View view, Context context, String productName, String productDescription) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(productName);
        builder.setMessage(productDescription);
        builder.setIcon(R.drawable.ic_info);

        /*builder.setNeutralButton("MORE INFO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Uri webpage = Uri.parse(workshopUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        if (!isAlreadyApplied(_ID)) {
            builder.setPositiveButton("Add to favorites", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (preferences.isLoggedIn()) {
                        handleAppliedVisibility(true, appliedButton);
                        insertIntoDashboard(context, _ID, workshopName);
                    } else {
                        Toast.makeText(context, "Please login to continue", Toast.LENGTH_SHORT).show();
                        view.getContext().startActivity(new Intent(view.getContext(), AuthActivity.class));
                    }
                }
            });
        } // .. Set apply button on dialog if user not applied for the communities
*/
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
