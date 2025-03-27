package com.example.appsignature;

public class Signature {
    private int id; // ID único para cada firma
    private String description; // Descripción de la firma
    private byte[] digitalSignature; // Firma almacenada como BLOB

    // Constructor vacío
    public Signature() {
    }

    // Constructor con parámetros
    public Signature(String description, byte[] digitalSignature) {
        this.description = description;
        this.digitalSignature = digitalSignature;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getDigitalSignature() {
        return digitalSignature;
    }

    public void setDigitalSignature(byte[] digitalSignature) {
        this.digitalSignature = digitalSignature;
    }
}
