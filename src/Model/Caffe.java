/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.io.InputStream;

/**
 *
 * @author syafi
 */
public class Caffe {
    
    public int id;
    protected String kategori;
    protected String daerah;
    protected String namaCaffe;
    protected String deskripsi;
    protected String alamat;
    protected String linkMaps;
    private String imagePath;
    private InputStream gambarStream;
    
    public Caffe(){
        
    }
    public Caffe(int id, String kategori, String daerah, String namaCaffe, String deskripsi, String alamat, String linkMaps, String imagePath){
        this.id = id;
        this.kategori = kategori;
        this.daerah = daerah;
        this.namaCaffe= namaCaffe;
        this.deskripsi = deskripsi;
        this.alamat = alamat;
        this.linkMaps = linkMaps;
        this.imagePath = imagePath;
        
    }
    public void setIdCaffe(int id) {
        this.id = id;
    }
    public int getIdCaffe() {
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
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getImagePath() {
        return imagePath;
    }
    
    public InputStream getGambarStream() {
        return gambarStream;
    }
    
    public void setGambarStream(InputStream gambarStream) {
        this.gambarStream = gambarStream;
    }
    
    public void tampilkanData(){
        System.out.println("\n=== Data Caffe ===");
        System.out.println("id Caffe : " + id);
        System.out.println("Kategori : " + kategori);
        System.out.println("Daerah : " + daerah);
        System.out.println("Nama Caffe : " + namaCaffe);
        System.out.println("Deskripsi : " + deskripsi);
        System.out.println("Alamat : " + alamat);
        System.out.println("Link Maps : " + linkMaps);
    }
    
}






