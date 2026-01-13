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
    


    private InputStream gambarStream;
    private byte[] gambarData; 
    public Caffe() {}


    public Caffe(int id, String kategori, String daerah, String namaCaffe, String deskripsi, String alamat, String linkMaps) {
        this.id = id;
        this.kategori = kategori;
        this.daerah = daerah;
        this.namaCaffe = namaCaffe;
        this.deskripsi = deskripsi;
        this.alamat = alamat;
        this.linkMaps = linkMaps;
    }
    

    public Caffe(int id, String kategori, String daerah, String namaCaffe, String deskripsi, String alamat, String linkMaps, String idPictureIgnored) {
        this(id, kategori, daerah, namaCaffe, deskripsi, alamat, linkMaps);
        // String idPictureIgnored tidak disimpan karena kita pakai gambarData (byte)
    }


    public int getId() {
        return id; 
    }
    public void setId(int id){
        this.id = id;
    }

    public String getKategori(){
        return kategori;
    }
    
    public void setKategori(String kategori){
        this.kategori = kategori;
    }

    public String getDaerah(){
        return daerah;
    }
    
    public void setDaerah(String daerah){
        this.daerah = daerah;
    }

    public String getNamaCaffe(){
        return namaCaffe; 
    }
    public void setNamaCaffe(String namaCaffe){
        this.namaCaffe = namaCaffe;
    }

    public String getDeskripsi(){
        return deskripsi;
    }
    public void setDeskripsi(String deskripsi){
        this.deskripsi = deskripsi; 
    }

    public String getAlamat(){
        return alamat;
    }
    
    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public String getLinkMaps(){
        return linkMaps;
    }
    public void setLinkMaps(String linkMaps) { this.linkMaps = linkMaps; }


    
    
    public InputStream getGambarStream() {
        return gambarStream;
    }
    public void setGambarStream(InputStream gambarStream) {
        this.gambarStream = gambarStream;
    }

    
    public byte[] getGambarData() {
        return gambarData;
    }
    public void setGambarData(byte[] gambarData) {
        this.gambarData = gambarData;
    }


    public boolean hasImageLoaded() {
        return gambarData != null && gambarData.length > 0;
    }
    

    
    @Override
    public String toString() {
        return namaCaffe + " (" + daerah + ")";
    }
    

    
    
    
    
    public void debugPrint() {
        System.out.println("Caffe: " + namaCaffe);
        System.out.println("- Stream Upload: " + (gambarStream != null ? "Ada" : "Kosong"));
        System.out.println("- Data Download: " + (hasImageLoaded() ? gambarData.length + " bytes" : "Belum di-load"));
    }
}

