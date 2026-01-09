

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.InputStream;

/**
 *
 * @author dwife
 */
public class Caffe {
    
    private int id;
    private String kategori;
    private String daerah;
    private String namaCaffe;
    private String deskripsi;
    private String alamat;
    private String linkMaps;
    private String idPicture;
    private InputStream gambarStream;
    
    public Caffe(){
        
    }

    public Caffe(String kategori, String daerah, String namaCaffe, String deskripsi, String alamat, String linkMaps) {
        this.kategori = kategori;
        this.daerah = daerah;
        this.namaCaffe = namaCaffe;
        this.deskripsi = deskripsi;
        this.alamat = alamat;
        this.linkMaps = linkMaps;
    }
    

    public Caffe(int id, String kategori, String daerah, String namaCaffe, String deskripsi, String alamat, String linkMaps, String idPicture){
        this.id = id;
        this.kategori = kategori;
        this.daerah = daerah;
        this.namaCaffe = namaCaffe;
        this.deskripsi = deskripsi;
        this.alamat = alamat;
        this.linkMaps = linkMaps;
        this.idPicture = idPicture;
    }
    

    public Caffe(int id, String kategori, String daerah, String namaCaffe, String deskripsi, String alamat, String linkMaps, InputStream gambarStream) {
        this.id = id;
        this.kategori = kategori;
        this.daerah = daerah;
        this.namaCaffe = namaCaffe;
        this.deskripsi = deskripsi;
        this.alamat = alamat;
        this.linkMaps = linkMaps;
        this.gambarStream = gambarStream;
    }
    

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
    public String getKategori() {
        return kategori;
    }
    
    public void setDaerah(String daerah) {
        this.daerah = daerah;
    }
    public String getDaerah() {
        return daerah;
    }

    public void setNamaCaffe(String namaCaffe) {
        this.namaCaffe = namaCaffe;
    }
    public String getNamaCaffe() {
        return namaCaffe;
    }
    
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
    public String getDeskripsi() {
        return deskripsi;
    }
    

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
    public String getAlamat() {
        return alamat;
    }

    public void setLinkMaps(String linkMaps) {
        this.linkMaps = linkMaps;
    }
    public String getLinkMaps() {
        return linkMaps;
    }
    

    public void setIdPicture(String idPicture) {
        this.idPicture = idPicture;
    }
    public String getIdPicture() {
        return idPicture;
    }
    

    public void setImagePath(String imagePath) {
        this.idPicture = imagePath;
    }
    public String getImagePath() {
        return idPicture;
    }
    

    public InputStream getGambarStream() {
        return gambarStream;
    }
    public void setGambarStream(InputStream gambarStream) {
        this.gambarStream = gambarStream;
    }
    

    public void tampilkanData(){
        System.out.println("\n=== Data Caffe ===");
        System.out.println("ID Caffe : " + id);
        System.out.println("Kategori : " + kategori);
        System.out.println("Daerah : " + daerah);
        System.out.println("Nama Caffe : " + namaCaffe);
        System.out.println("Deskripsi : " + deskripsi);
        System.out.println("Alamat : " + alamat);
        System.out.println("Link Maps : " + linkMaps);
        System.out.println("ID Picture/Path : " + (idPicture != null ? idPicture : "Tidak ada"));
    }
    

    @Override
    public String toString() {
        return namaCaffe + " - " + daerah + " (" + kategori + ")";
    }
}
