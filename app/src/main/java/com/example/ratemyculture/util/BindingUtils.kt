package com.example.ratemyculture.util



import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@BindingAdapter("app:src")
fun setImageUri(imageView: CircleImageView, url: String?) {
    if (url != null) {
        Picasso.get().load(url).into(imageView)
    }
}

@BindingAdapter("app:imageBitmap")
fun setImageBitmap(imageView: AppCompatImageView, bitmap: Bitmap?) {
    if (bitmap != null) {
        imageView.setImageBitmap(bitmap)
    }
}





