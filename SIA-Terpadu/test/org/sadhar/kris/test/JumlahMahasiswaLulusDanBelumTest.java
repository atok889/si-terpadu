/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.kris.test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfree.data.category.DefaultCategoryDataset;
import org.junit.Test;
import org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus.JumlahMahasiswaLulusDanBelumLulus;
import org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus.JumlahMahasiswaLulusDanBelumLulusDAO;
import org.sadhar.sia.terpadu.jumlahmahasiswalulusdanbelumlulus.JumlahMahasiswaLulusDanBelumLulusDAOImpl;
import org.sadhar.util.DBConnection;

/**
 *
 * @author kris
 */
public class JumlahMahasiswaLulusDanBelumTest {

    private String kodeProdi = "1114";
    private JumlahMahasiswaLulusDanBelumLulusDAO jumlahMahasiswaLulusDanBelumLulusDAO;

    public JumlahMahasiswaLulusDanBelumTest() {
        DBConnection.initDataSource();
        jumlahMahasiswaLulusDanBelumLulusDAO = new JumlahMahasiswaLulusDanBelumLulusDAOImpl();
    }

    @Test
    public void getData() {
        List<Map> mahasiswas = new ArrayList<Map>();
        List<Map> jumlahMahasiswas = jumlahMahasiswaLulusDanBelumLulusDAO.getJumlahMahasiswa(kodeProdi);
        List<Map> jumlahLulus = jumlahMahasiswaLulusDanBelumLulusDAO.getJumlahMahasiswaLulus(kodeProdi);
        for (int i = 2000; i <= 2010; i++) {
            double jumlah = 0;
            int angkatan = 0;
            double jumlahMahasiswa = 0;
            double jumlahMahasiswaLulus = 0;
            double prosentasiMahasiswa = 0d;
            double prosentaseLulus = 0d;
            double prosentasiBelumLulus = 0d;
            Map mahasiswa = new HashMap();

            if (jumlahMahasiswas.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Jumlah Mahasiswa");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : jumlahMahasiswas) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Mahasiswa");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Mahasiswa");
                        }
                    }
                }
                jumlahMahasiswa = (Double) mahasiswa.get("jumlah");
            }
            mahasiswas.add(mahasiswa);

            if (jumlahLulus.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Jumlah Lulus");
            } else {
                jumlah = 0;
                mahasiswa = new HashMap();
                for (Map map : jumlahLulus) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Lulus");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", jumlah);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Lulus");
                        }
                    }
                }
                jumlahMahasiswaLulus = (Double) mahasiswa.get("jumlah");
            }
            mahasiswas.add(mahasiswa);

            if (jumlahMahasiswas.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Prosentase Lulus");
            } else {
                mahasiswa = new HashMap();
                prosentasiMahasiswa = (jumlahMahasiswa * 10 / 100);
                if (prosentasiMahasiswa != 0) {
                    prosentaseLulus = 100 - ((jumlahMahasiswa - jumlahMahasiswaLulus) / prosentasiMahasiswa) * 10;
                }
                for (Map map : jumlahMahasiswas) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", prosentaseLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Prosentase Lulus");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", prosentaseLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Prosentase Lulus");
                        }
                    }
                }
            }
            mahasiswas.add(mahasiswa);

            if (jumlahMahasiswas.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Jumlah Belum Lulus");
            } else {
                mahasiswa = new HashMap();
                for (Map map : jumlahMahasiswas) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", jumlahMahasiswa - jumlahMahasiswaLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Belum Lulus");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", jumlahMahasiswa - jumlahMahasiswaLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Jumlah Belum Lulus");
                        }
                    }
                }
            }
            mahasiswas.add(mahasiswa);

            if (jumlahMahasiswas.size() == 0) {
                mahasiswa = new HashMap();
                mahasiswa.put("jumlah", 0d);
                mahasiswa.put("tahun", i);
                mahasiswa.put("header", "Prosentase Belum Lulus");
            } else {
                mahasiswa = new HashMap();
                if (jumlahMahasiswa - jumlahMahasiswaLulus == 0) {
                    prosentasiBelumLulus = 0;
                } else {
                    prosentasiBelumLulus = 100 - prosentaseLulus;
                }
                for (Map map : jumlahMahasiswas) {
                    if (!map.get("angkatan").toString().matches(".*[A-Z].*")) {
                        angkatan = Integer.parseInt(map.get("angkatan").toString());
                        if (angkatan != i) {
                            angkatan = i;
                            mahasiswa.put("jumlah", prosentasiBelumLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Prosentase Belum Lulus");
                        } else {
                            jumlah += Double.parseDouble(map.get("jumlah").toString());
                            mahasiswa.put("jumlah", prosentasiBelumLulus);
                            mahasiswa.put("tahun", angkatan);
                            mahasiswa.put("header", "Prosentase Belum Lulus");
                        }
                    }
                }
            }
            mahasiswas.add(mahasiswa);
        }
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        List<JumlahMahasiswaLulusDanBelumLulus> data = new ArrayList<JumlahMahasiswaLulusDanBelumLulus>();
        for (Map map : mahasiswas) {
            System.out.println(map.get("tahun") + "::" + map.get("jumlah") + "::" + map.get("header"));
            Double jml = 0d;
            try {
                jml = (Double) map.get("jumlah");
            } catch (NullPointerException npe) {
                jml = 0d;
            }
            dataset.addValue(jml, map.get("tahun").toString(), map.get("header").toString());

            DecimalFormat dfJumlah = new DecimalFormat("# 0;-0");
            DecimalFormat dfProsen = new DecimalFormat("# 0.00");
            String jumlah = "";
            String header = "";
            if (map.get("header").toString().equalsIgnoreCase("Jumlah Mahasiswa") || map.get("header").toString().equalsIgnoreCase("Jumlah Belum Lulus") || map.get("header").toString().equalsIgnoreCase("Jumlah Lulus")) {
                jumlah = dfJumlah.format(jml);
                header = map.get("header").toString();
            } else {
                if (map.get("header").toString().equalsIgnoreCase("Prosentase Lulus")) {
                    jumlah = dfProsen.format(jml) + "%";
                    header = "Prosentase Lulus";
                } else {
                    jumlah = dfProsen.format(jml) + "%";
                    header = "Prosentase Belum Lulus";
                }
            }
            String tahun = map.get("tahun").toString();


            JumlahMahasiswaLulusDanBelumLulus jmldbl = new JumlahMahasiswaLulusDanBelumLulus();
            jmldbl.setTahun(map.get("tahun").toString());
            jmldbl.setJumlah(jumlah);
            jmldbl.setHeader(header);
            data.add(jmldbl);


        }

        for (JumlahMahasiswaLulusDanBelumLulus o : data) {
            System.out.println(o.getTahun() + "::" + o.getHeader() + "::" + o.getJumlah());
        }




        List<Map> datas = new ArrayList<Map>();
        for (Object row : dataset.getRowKeys()) {
            System.out.print(row.toString() + "::");
        }

        System.out.println("");
        for (Object column : dataset.getColumnKeys()) {
            System.out.print(column.toString() + "::");
            int i = 1;
            for (Object row : dataset.getRowKeys()) {
                Number number = dataset.getValue((Comparable) row, (Comparable) column);
                if (number == null) {
                    number = 0d;
                } else {
                    System.out.print("|" + number.doubleValue());
                }
                i++;
            }
            System.out.println("");
        }
    }
}

