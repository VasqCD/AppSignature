package com.example.appsignature;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.cardview.widget.CardView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SignatureAdapter extends RecyclerView.Adapter<SignatureAdapter.SignatureViewHolder> {
    private final List<Signature> signatures;

    public SignatureAdapter(List<Signature> signatures) {
        this.signatures = signatures;
    }

    @NonNull
    @Override
    public SignatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_signature, parent, false);
        return new SignatureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SignatureViewHolder holder, int position) {
        Signature signature = signatures.get(position);

        // Establecer descripción
        holder.tvDescription.setText(signature.getDescription());

        // Convertir BLOB a Bitmap
        if (signature.getDigitalSignature() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(
                    signature.getDigitalSignature(),
                    0,
                    signature.getDigitalSignature().length
            );
            holder.ivSignature.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return signatures.size();
    }

    public static class SignatureViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription;
        ImageView ivSignature;

        public SignatureViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivSignature = itemView.findViewById(R.id.ivSignature);
        }
    }
}
