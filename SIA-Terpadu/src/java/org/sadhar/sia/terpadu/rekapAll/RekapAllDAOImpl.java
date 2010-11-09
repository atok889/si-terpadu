/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.rekapAll;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.sadhar.sia.common.ClassConnection;
import org.springframework.jdbc.core.RowMapper;
import org.zkoss.zhtml.Messagebox;

/**
 *     
 * @author Dian 
 */
public class RekapAllDAOImpl implements RekapAllDAO {

    public RekapAllDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    /* =========================================================================
     * @method untuk menampilkan daftar unit
     * =========================================================================
     */
    public List getDaftarUnit(String kata) {
        return ClassConnection.getJdbc().query("" +
                "SELECT Kd_unit_kerja,Nama_unit_kerja " +
                "FROM kamus.unkerja " +
                "WHERE Nama_unit_kerja LIKE '%"+kata+"%' " +
                "ORDER BY Nama_unit_kerja"
                + "", new DaftarUnit());
    }

    protected class DaftarUnit implements RowMapper {

        public Object mapRow(ResultSet rs, int arg1) throws SQLException {
            RekapAll data = new RekapAll();
            data.setKodeUnit(rs.getString("Kd_unit_kerja"));
            data.setNamaUnit(rs.getString("Nama_unit_kerja"));
            return data;
        }
    }

//    /* ==================================================================================
//     * @method untuk mengambil data detail pengajuan pengambilan berdasarkan idPengajuan
//     * ==================================================================================
//     */
//    public List getDaftarPengajuanPencairanDana_PerIdPengajuan(String idpengajuan) {
//        return ClassConnection.getJdbc().query(""
//                + "SELECT dpp.idDetailPengajuanPengambilan,"
//                + "dpp.idPengajuan,"
//                + "dpp.kodeJenisAnggaran,"
//                + "jenisBiaya,"
//                + "dpp.kodePosBiaya,"
//                + "dpp.kodeItemBiaya,"
//                + "dpp.kodeSubKegiatan,"
//                + "dpp.kodeSub,"
//                + "dpp.kodeSubItemBiaya,"
//                + "dpp.kuantitasPengajuan,"
//                + "dpp.kuantitasDisetujui,"
//                + "dpp.jumlahHariPengajuan,"
//                + "dpp.jumlahHariDisetujui,"
//                + "dpp.harga,"
//                + "dpp.namaItemDiluarKamus,"
//                + "(select noSTNK from asset.kendaraan k where kodeKendaraan = kodeSubItemBiaya) AS subitembiaya,"
//                + "(select namaPosBiaya from ppmk.posBiaya pb where pb.kodePosBiaya = dpp.kodePosBiaya) AS posbiaya,"
//                + "(select namaPosBiaya from ppmk.posBiaya pb where pb.kodePosBiaya = dpp.kodeSub) AS subposbiaya "
//                + "FROM ppmk.detailPengajuanPengambilan dpp "
//                + "	LEFT JOIN ppmk.jenisBiaya jb ON jb.kodeJenisBiaya = dpp.kodeJenisAnggaran "
//                + "     LEFT JOIN ppmk.pengajuanPengambilan pp ON dpp.idPengajuan = pp.idPengajuan "
//                + "WHERE dpp.idPengajuan = '" + idpengajuan + "' "
//                + "ORDER BY kodeJenisAnggaran,kodeSubKegiatan,posbiaya,subposbiaya"
//                + "", new DaftarPengajuanPencairanDana_PerIdPengajuan());
//    }
//
//    protected class DaftarPengajuanPencairanDana_PerIdPengajuan implements RowMapper {
//
//        public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//            RekapLpj data = new RekapLpj();
//            data.setIdDetailPengajuanPengambilan(rs.getString("idDetailPengajuanPengambilan"));
//            data.setIdPengajuan(rs.getString("idPengajuan"));
//            data.setKodeJenisBiaya(rs.getString("kodeJenisAnggaran"));
//            data.setJenisBiaya(rs.getString("jenisBiaya"));
//            data.setKodePosBiaya(rs.getString("kodePosBiaya"));
//            data.setKodeItemBiaya(rs.getString("kodeItemBiaya"));
//            data.setKodeSubKegiatan(rs.getString("kodeSubKegiatan"));
//            data.setKodeSubPosBiaya(rs.getString("kodeSub"));
//            data.setKodeSubItemBiaya(rs.getString("kodeSubItemBiaya"));
//            data.setKuantitasPengajuan(rs.getFloat("kuantitasPengajuan"));
//            data.setKuantitasDisetujui(rs.getFloat("kuantitasDisetujui"));
//            data.setJumlahHariPengajuan(rs.getString("jumlahHariPengajuan"));
//            data.setJumlahHariDisetujui(rs.getString("jumlahHariDisetujui"));
//            data.setHarga(rs.getFloat("harga"));
//            data.setSubItemBiaya(rs.getString("subitembiaya"));
//            data.setPosBiaya(rs.getString("posbiaya"));
//            data.setSubPosBiaya(rs.getString("subposbiaya"));
//            data.setNamaItemDiluarKamus(rs.getString("namaItemDiluarKamus"));
//            return data;
//        }
//    }
//
//    /* =========================================================================
//     * @method untuk menampilkan daftar daftar hasil pengajuan Pencairan Dana unit
//     * =========================================================================
//     */
//    // berdasarkan unit
//    public List getDaftarHasilPengajuanPencairanDana_BerdasarkanUnit(String tahun,String kodeunit) {
//        return ClassConnection.getJdbc().query("" +
//                "SELECT pp.idPengajuan,pp.tahunAnggaran,pp.tglPengajuan,pp.keteranganPenggunaan,pp.kodeUnitPengaju," +
//                "pp.tanggalKirimKeBiroKeuangan,pp.tanggalProsesBiroKeuangan,pp.tglSelesaiProses,uk.Nama_unit_kerja," +
//                "pp.noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan pp " +
//                "     LEFT JOIN kamus.unkerja uk ON pp.kodeUnitPengaju = uk.Kd_unit_kerja " +
//                "WHERE pp.tahunAnggaran = '"+tahun+"' " +
//                "   AND (pp.tglSelesaiProses IS NOT NULL OR pp.tglSelesaiProses != '0000-00-00') " +
//                "   AND pp.kodeUnitPengaju = '"+kodeunit+"' " +
//                "ORDER BY uk.Nama_unit_kerja,pp.keteranganPenggunaan,pp.tglPengajuan"
//                + "", new DaftarHasil());
//    }
//
//    // berdasarkan tanggal
//    public List getDaftarHasilPengajuanPencairanDana_BerdasarkanTanggal(String tahun,String tanggalawal,String tanggalakhir) {
//        return ClassConnection.getJdbc().query("" +
//                "SELECT pp.idPengajuan,pp.tahunAnggaran,pp.tglPengajuan,pp.keteranganPenggunaan,pp.kodeUnitPengaju," +
//                "pp.tanggalKirimKeBiroKeuangan,pp.tanggalProsesBiroKeuangan,pp.tglSelesaiProses,uk.Nama_unit_kerja, " +
//                "pp.noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan pp " +
//                "     LEFT JOIN kamus.unkerja uk ON pp.kodeUnitPengaju = uk.Kd_unit_kerja " +
//                "WHERE pp.tahunAnggaran = '"+tahun+"' " +
//                "   AND (pp.tglSelesaiProses IS NOT NULL OR pp.tglSelesaiProses != '0000-00-00') " +
//                "   AND (pp.tglPengajuan BETWEEN '"+tanggalawal+"' AND '"+tanggalakhir+"') " +
//                "ORDER BY uk.Nama_unit_kerja,pp.keteranganPenggunaan,pp.tglPengajuan"
//                + "", new DaftarHasil());
//    }
//
//    // berdasarkan danauntuk
//    public List getDaftarHasilPengajuanPencairanDana_BerdasarkanDanaUntuk(String tahun,String danauntuk) {
//        return ClassConnection.getJdbc().query("" +
//                "SELECT pp.idPengajuan,pp.tahunAnggaran,pp.tglPengajuan,pp.keteranganPenggunaan,pp.kodeUnitPengaju," +
//                "pp.tanggalKirimKeBiroKeuangan,pp.tanggalProsesBiroKeuangan,pp.tglSelesaiProses,uk.Nama_unit_kerja, " +
//                "pp.noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan pp " +
//                "     LEFT JOIN kamus.unkerja uk ON pp.kodeUnitPengaju = uk.Kd_unit_kerja " +
//                "WHERE pp.tahunAnggaran = '"+tahun+"' " +
//                "   AND (pp.tglSelesaiProses IS NOT NULL OR pp.tglSelesaiProses != '0000-00-00') " +
//                "   AND pp.keteranganPenggunaan LIKE '%"+danauntuk+"%' " +
//                "ORDER BY uk.Nama_unit_kerja,pp.keteranganPenggunaan,pp.tglPengajuan"
//                + "", new DaftarHasil());
//    }
//
//    // berdasarkan unit dan tanggal
//    public List getDaftarHasilPengajuanPencairanDana_BerdasarkanUnitTanggal(String tahun,String kodeunit,String tanggalawal,String tanggalakhir) {
//        return ClassConnection.getJdbc().query("" +
//                "SELECT pp.idPengajuan,pp.tahunAnggaran,pp.tglPengajuan,pp.keteranganPenggunaan,pp.kodeUnitPengaju," +
//                "pp.tanggalKirimKeBiroKeuangan,pp.tanggalProsesBiroKeuangan,pp.tglSelesaiProses,uk.Nama_unit_kerja, " +
//                "pp.noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan pp " +
//                "     LEFT JOIN kamus.unkerja uk ON pp.kodeUnitPengaju = uk.Kd_unit_kerja " +
//                "WHERE pp.tahunAnggaran = '"+tahun+"' " +
//                "   AND (pp.tglSelesaiProses IS NOT NULL OR pp.tglSelesaiProses != '0000-00-00') " +
//                "   AND pp.kodeUnitPengaju = '"+kodeunit+"' " +
//                "   AND (pp.tglPengajuan BETWEEN '"+tanggalawal+"' AND '"+tanggalakhir+"') " +
//                "ORDER BY uk.Nama_unit_kerja,pp.keteranganPenggunaan,pp.tglPengajuan"
//                + "", new DaftarHasil());
//    }
//
//    // berdasarkan unit dan danauntuk
//    public List getDaftarHasilPengajuanPencairanDana_BerdasarkanUnitDanaUntuk(String tahun,String kodeunit,String danauntuk) {
//        return ClassConnection.getJdbc().query("" +
//                "SELECT pp.idPengajuan,pp.tahunAnggaran,pp.tglPengajuan,pp.keteranganPenggunaan,pp.kodeUnitPengaju," +
//                "pp.tanggalKirimKeBiroKeuangan,pp.tanggalProsesBiroKeuangan,pp.tglSelesaiProses,uk.Nama_unit_kerja, " +
//                "pp.noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan pp " +
//                "     LEFT JOIN kamus.unkerja uk ON pp.kodeUnitPengaju = uk.Kd_unit_kerja " +
//                "WHERE pp.tahunAnggaran = '"+tahun+"' " +
//                "   AND (pp.tglSelesaiProses IS NOT NULL OR pp.tglSelesaiProses != '0000-00-00') " +
//                "   AND pp.kodeUnitPengaju = '"+kodeunit+"' " +
//                "   AND pp.keteranganPenggunaan LIKE '%"+danauntuk+"%' " +
//                "ORDER BY uk.Nama_unit_kerja,pp.keteranganPenggunaan,pp.tglPengajuan"
//                + "", new DaftarHasil());
//    }
//
//    // berdasarkan tanggal dan danauntuk
//    public List getDaftarHasilPengajuanPencairanDana_BerdasarkanTanggalDanaUntuk(String tahun,String tanggalawal,String tanggalakhir,String danauntuk) {
//        return ClassConnection.getJdbc().query("" +
//                "SELECT pp.idPengajuan,pp.tahunAnggaran,pp.tglPengajuan,pp.keteranganPenggunaan,pp.kodeUnitPengaju," +
//                "pp.tanggalKirimKeBiroKeuangan,pp.tanggalProsesBiroKeuangan,pp.tglSelesaiProses,uk.Nama_unit_kerja, " +
//                "pp.noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan pp " +
//                "     LEFT JOIN kamus.unkerja uk ON pp.kodeUnitPengaju = uk.Kd_unit_kerja " +
//                "WHERE pp.tahunAnggaran = '"+tahun+"' " +
//                "   AND (pp.tglSelesaiProses IS NOT NULL OR pp.tglSelesaiProses != '0000-00-00') " +
//                "   AND (pp.tglPengajuan BETWEEN '"+tanggalawal+"' AND '"+tanggalakhir+"') " +
//                "   AND pp.keteranganPenggunaan LIKE '%"+danauntuk+"%' " +
//                "ORDER BY uk.Nama_unit_kerja,pp.keteranganPenggunaan,pp.tglPengajuan"
//                + "", new DaftarHasil());
//    }
//
//    // berdasarkan unit, tanggal dan danauntuk
//    public List getDaftarHasilPengajuanPencairanDana_BerdasarkanSemua(String tahun,String kodeunit,String tanggalawal,String tanggalakhir,String danauntuk) {
//        return ClassConnection.getJdbc().query("" +
//                "SELECT pp.idPengajuan,pp.tahunAnggaran,pp.tglPengajuan,pp.keteranganPenggunaan,pp.kodeUnitPengaju," +
//                "pp.tanggalKirimKeBiroKeuangan,pp.tanggalProsesBiroKeuangan,pp.tglSelesaiProses,uk.Nama_unit_kerja, " +
//                "pp.noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan pp " +
//                "     LEFT JOIN kamus.unkerja uk ON pp.kodeUnitPengaju = uk.Kd_unit_kerja " +
//                "WHERE pp.tahunAnggaran = '"+tahun+"' " +
//                "   AND (pp.tglSelesaiProses IS NOT NULL OR pp.tglSelesaiProses != '0000-00-00') " +
//                "   AND pp.kodeUnitPengaju = '"+kodeunit+"' " +
//                "   AND (pp.tglPengajuan BETWEEN '"+tanggalawal+"' AND '"+tanggalakhir+"') " +
//                "   AND pp.keteranganPenggunaan LIKE '%"+danauntuk+"%' " +
//                "ORDER BY uk.Nama_unit_kerja,pp.keteranganPenggunaan,pp.tglPengajuan"
//                + "", new DaftarHasil());
//    }
//
//    // berdasarkan semua
//    public List getDaftarHasilPengajuanPencairanDana(String tahun) {
//        return ClassConnection.getJdbc().query("" +
//                "SELECT pp.idPengajuan,pp.tahunAnggaran,pp.tglPengajuan,pp.keteranganPenggunaan,pp.kodeUnitPengaju," +
//                "pp.tanggalKirimKeBiroKeuangan,pp.tanggalProsesBiroKeuangan,pp.tglSelesaiProses,uk.Nama_unit_kerja, " +
//                "pp.noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan pp " +
//                "     LEFT JOIN kamus.unkerja uk ON pp.kodeUnitPengaju = uk.Kd_unit_kerja " +
//                "WHERE pp.tahunAnggaran = '"+tahun+"' " +
//                "   AND (pp.tglSelesaiProses IS NOT NULL OR pp.tglSelesaiProses != '0000-00-00') " +
//                "ORDER BY uk.Nama_unit_kerja,pp.keteranganPenggunaan,pp.tglPengajuan"
//                + "", new DaftarHasil());
//    }
//
//    protected class DaftarHasil implements RowMapper {
//
//        public Object mapRow(ResultSet rs, int arg1) throws SQLException {
//            RekapLpj data = new RekapLpj();
//            data.setIdPengajuan(rs.getString("idPengajuan"));
//            data.setTahunAnggaran(rs.getString("tahunAnggaran"));
//            data.setTglPengajuan(rs.getString("tglPengajuan"));
//            data.setDanaUntuk(rs.getString("keteranganPenggunaan"));
//            data.setKodeUnit(rs.getString("kodeUnitPengaju"));
//            data.setTglKirimKeBiroKeu(rs.getString("tanggalKirimKeBiroKeuangan"));
//            data.setTglProses(rs.getString("tanggalProsesBiroKeuangan"));
//            data.setTglSelesaiProses(rs.getString("tglSelesaiProses"));
//            data.setNamaUnit(rs.getString("Nama_unit_kerja"));
//            data.setNoPinjaman(rs.getString("noPinjaman"));
//            return data;
//        }
//    }
//
//    // STRING ==================================================================
//
//    /*
//     * @Method untuk menampilkan namarapatkerja
//     */
//    public String getNamaRapatKerja(String kodesubkegiatan) {
//        String xStr = "select namaRapatKerja "
//                + "from ppmk.rapatKerjaUnit rku "
//                + "      left join ppmk.rapatKerja rk on rk.kodeRapatKerja = rku.kodeRapatKerja "
//                + "where idRapatKerjaUnit = '" + kodesubkegiatan + "'";
//        SqlRowSet rs = (SqlRowSet) ClassConnection.getJdbc().queryForRowSet(xStr);
//        if (rs.next()) {
//            return rs.getString("namaRapatKerja");
//        } else {
//            return "";
//        }
//    }
//
//    /*
//     * @Method untuk menampilkan namakegiatanunit
//     */
//    public String getNamaKegiatanUnit(String kodesubkegiatan) {
//        String xStr = "select namaSubKegiatan "
//                + "from ppmk.kegiatanUnit "
//                + "where kodeKegiatanUnit = '" + kodesubkegiatan + "'";
//        SqlRowSet rs = (SqlRowSet) ClassConnection.getJdbc().queryForRowSet(xStr);
//        if (rs.next()) {
//            return rs.getString("namaSubKegiatan");
//        } else {
//            return "";
//        }
//    }
//
//    /*
//     * @Method untuk menampilkan namaTabelItem dari tabel ppmk.posBiaya
//     */
//    public String getNamaTabelItem(String kodeposbiaya) {
//        String xStr = "SELECT namaTabelItem "
//                + "FROM ppmk.posBiaya "
//                + "WHERE kodePosBiaya = '" + kodeposbiaya + "'";
//        SqlRowSet rs = (SqlRowSet) ClassConnection.getJdbc().queryForRowSet(xStr);
//        if (rs.next()) {
//            return rs.getString("namaTabelItem");
//        } else {
//            return "";
//        }
//    }
//
//    /*
//     * @method untuk mengambil nama barang
//     */
//    public String getNamaBarang(String kodebarang) {
//        String xStr = "select b.namaBarang "
//                + "from asset.hargaBarang hb "
//                + "	left join asset.barang b on hb.idBarang = b.idBarang "
//                + "where hb.idBarang = '" + kodebarang + "'";
//        SqlRowSet rs = (SqlRowSet) ClassConnection.getJdbc().queryForRowSet(xStr);
//        if (rs.next()) {
//            return rs.getString("namaBarang");
//        } else {
//            return "";
//        }
//    }
//
//    /*
//     * @Method untuk menampilkan namaitembiaya
//     */
//    public String getNamaItem(String kodeitem, String kodepos) {
//        String xStr = "select namaItem "
//                + "from ppmk.itemBiaya "
//                + "where kodeItem = '" + kodeitem + "' and kodePosBiaya = '" + kodepos + "'";
//        SqlRowSet rs = (SqlRowSet) ClassConnection.getJdbc().queryForRowSet(xStr);
//        if (rs.next()) {
//            return rs.getString("namaItem");
//        } else {
//            return "";
//        }
//    }
//
//    /*
//     * @Method untuk menampilkan Nomor STNK
//     */
//    public String getNoSTNK(String kodekendaraan) {
//        String xStr = "select noSTNK "
//                + "from asset.kendaraan "
//                + "where kodeKendaraan = '" + kodekendaraan + "'";
//        SqlRowSet rs = (SqlRowSet) ClassConnection.getJdbc().queryForRowSet(xStr);
//        if (rs.next()) {
//            return rs.getString("noSTNK");
//        } else {
//            return "";
//        }
//    }
//
//    /*
//     * @Method untuk mendapatkan noPinjaman dari tabel ppmk.pengajuanPengambilan sesuai idPengajuan
//     */
//    public String getNoPinjaman(String tahunanggaran,String idpengajuan) {
//        String xStr = "SELECT noPinjaman " +
//                "FROM ppmk.pengajuanPengambilan " +
//                "WHERE tahunAnggaran = '"+tahunanggaran+"' AND idPengajuan = '"+idpengajuan+"' ";
//        SqlRowSet rs = (SqlRowSet) ClassConnection.getJdbc().queryForRowSet(xStr);
//        if (rs.next()) {
//            return rs.getString("nopinjaman");
//        } else {
//            return "";
//        }
//    }
//
//    /*
//     * @Method untuk mengambil total pengajuan
//     */
//    public Long getTotalPengajuan(String idpengajuan) {
//        String xStr = "SELECT ROUND(SUM(if(d.jumlahHariPengajuan = 0,(d.kuantitasPengajuan*d.harga),(d.kuantitasPengajuan*d.harga*d.jumlahHariPengajuan)))) as biaya " +
//                "FROM ppmk.detailPengajuanPengambilan d " +
//                "   LEFT JOIN ppmk.pengajuanPengambilan p ON p.idPengajuan = d.idPengajuan " +
//                "WHERE p.idPengajuan = '"+idpengajuan+"' ";
//        SqlRowSet rs = (SqlRowSet) ClassConnection.getJdbc().queryForRowSet(xStr);
//        if (rs.next()) {
//            return rs.getLong("biaya");
//        } else {
//            return null;
//        }
//    }
//
//    // REPORT =================================================================
//
//    public void deleteTabelPengajuanPencairanDana() throws Exception {
//        ClassConnection.getJdbc().execute(
//                "DROP TABLE IF EXISTS tempo.tempPengajuanPencairanDana");
//    }
//
//    public void createTableTempPengajuanPencairanDana() {
//        String xStr = "Create table tempo.tempPengajuanPencairanDana("
//                + "jenisbiaya varchar(10),"
//                + "kegiatanrapat varchar(100),"
//                + "posbiaya varchar(100),"
//                + "subposbiaya varchar(100),"
//                + "itembiaya varchar(100),"
//                + "subitembiaya varchar(100),"
//                + "harga varchar(50),"
//                + "kuantitas varchar(10),"
//                + "jumlahhari varchar(10),"
//                + "nominal varchar(50) ) ";
//        ClassConnection.getJdbc().update(xStr);
//    }
//
//    public void setInsertPengajuanPencairanDana(RekapLpj xtemp) {
//        String xStr = " INSERT INTO tempo.tempPengajuanPencairanDana ("
//                + "jenisbiaya,"
//                + "kegiatanrapat,"
//                + "posbiaya,"
//                + "subposbiaya,"
//                + "itembiaya,"
//                + "subitembiaya,"
//                + "harga,"
//                + "kuantitas,"
//                + "jumlahhari,"
//                + "nominal) "
//                + "VALUES(?,?,?,?,?,?,?,?,?,?)";
//        Object[] params = new Object[]{
//            xtemp.getJenisBiaya(),
//            xtemp.getKegiatanRapat(),
//            xtemp.getPosBiaya(),
//            xtemp.getSubPosBiaya(),
//            xtemp.getItemBiaya(),
//            xtemp.getSubItemBiaya(),
//            xtemp.getTempHarga(),
//            xtemp.getTempKuantitasDisetujui(),
//            xtemp.getJumlahHariDisetujui(),
//            xtemp.getNominal()};
//        int types[] = new int[]{
//            Types.VARCHAR,
//            Types.VARCHAR,
//            Types.VARCHAR,
//            Types.VARCHAR,
//            Types.VARCHAR,
//            Types.VARCHAR,
//            Types.VARCHAR,
//            Types.VARCHAR,
//            Types.VARCHAR,
//            Types.VARCHAR};
//        ClassConnection.getJdbc().update(xStr, params, types);
//    }

    public List getLpj(String kdUnker, String thn) throws Exception {
        String xStr = "select * from "+
                "(select anggaran.no as no1, anggaran.nm as nm1, anggaran.kodepos as kodepos1, anggaran.namaPosBiaya as namaPosBiaya1,anggaran.anggaran as anggaran1, " +
                "pencairan.no as no2, pencairan.nm as nm2, pencairan.kodepos as kodepos2, pencairan.namaPosBiaya as namaPosBiaya2,pencairan.pengajuan as pengajuan2,pencairan.cair as cair2," +
                "lapPJ.no as no3, lapPJ.nm as nm3, lapPJ.kodepos as kodepos3,lapPJ.namaPosBiaya as namaPosBiaya3, lapPJ.pengajuan as pengajuan3,lapPJ.LPJ as LPJ3 " +
                "from " +
                "(select '1' as no,'RAB Non Rapat' as nm,rab.kodePosBiaya as kodepos,namaPosBiaya,sum(harga*kuantitas)as anggaran " +
                "from ppmk.anggaranRABNonRapatKerja as rab inner join ppmk.posBiaya as pos on rab.kodePosBiaya=pos.kodePosBiaya " +
                "where rab.kodeUnit='"+kdUnker+"' and tahun='"+thn+"' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rab.kodePosBiaya " +
                "union all " +
                "select '2' as no,'RAB Rapat Kerja' as nm,rab_rapat.kodePosBiaya as kodepos, namaPosBiaya,if(jumlahHari=0,sum(harga*kuantitas),sum(harga*kuantitas*jumlahHari))as anggaran " +
                "from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
                "inner join ppmk.rapatKerjaUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.idRapatKerjaUnit " +
                "where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='Y' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rab_rapat.kodePosBiaya " +
                "union all " +
                "select '3' as no, pos2.namaPosBiaya as nm,rab_rapat.kodePosBiaya as kodepos, " +
                "pos.namaPosBiaya,sum(if(jumlahHari=0,harga*kuantitas,harga*kuantitas*jumlahHari)) " +
                "from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
                "inner join ppmk.kegiatanUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.kodeKegiatanUnit " +
                "inner join ppmk.kelompokKegiatan as klpk on rpt.kodeKelompokKegiatan=klpk.kodeKelompok " +
                "inner join ppmk.kelompokRKA as kplkRKA on klpk.kodeKelompok=kplkRKA.kodeKelompokKegiatan " +
                "inner join ppmk.posBiaya as pos2 on kplkRKA.kodePosBiaya=pos2.kodePosBiaya " +
                "where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='N' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rpt.kodeKelompokKegiatan,rab_rapat.kodePosBiaya " +
                ") as anggaran " +
                "inner join " +
                "(select '1' as no,'RAB Non Rapat'as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
                "group by dp.kodePosBiaya " +
                "union all " +
                "select '2'as no,'RAB Rapat Kerja' as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
                "group by dp.kodePosBiaya,dp.kodeSub " +
                "union all " +
                "select '3' as no,pos2.namaPosBiaya,dp.kodeSub as kodepos,pos.namaPosBiaya, " +
                "sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
                "inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
                "group by dp.kodePosBiaya,dp.kodeSub) " +
                "as pencairan on anggaran.no=pencairan.no and anggaran.kodepos=pencairan.kodepos " +
                "inner join " +
                "(select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as LPJ " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
                "group by dp.kodePosBiaya " +
                "union all " +
                "select '2' as no,'RAB Rapat Kerja' as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
                "group by dp.kodePosBiaya,dp.kodeSubPos " +
                "union all " +
                "select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, " +
                "sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
                "inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
                "group by dp.kodePosBiaya,dp.kodeSubPos) lapPJ on pencairan.no=lapPJ.no and pencairan.kodepos=lapPJ.kodepos " +
                "UNION ALL " +
                "select anggaran.no, anggaran.nm, anggaran.kodepos, anggaran.namaPosBiaya, anggaran.anggaran, " +
                "pencairan.no, pencairan.nm, pencairan.kodepos, pencairan.namaPosBiaya,pencairan.pengajuan,pencairan.cair, " +
                "lapPJ.no, lapPJ.nm, lapPJ.kodepos, lapPJ.namaPosBiaya, lapPJ.pengajuan,lapPJ.LPJ " +
                "from " +
                "(select '1' as no,'RAB Non Rapat' as nm,rab.kodePosBiaya as kodepos,namaPosBiaya,sum(harga*kuantitas)as anggaran " +
                "from ppmk.anggaranRABNonRapatKerja as rab inner join ppmk.posBiaya as pos on rab.kodePosBiaya=pos.kodePosBiaya " +
                "where rab.kodeUnit='"+kdUnker+"' and tahun='"+thn+"' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rab.kodePosBiaya " +
                "union all " +
                "select '2' as no,'RAB Rapat Kerja' as nm,rab_rapat.kodePosBiaya as kodepos, namaPosBiaya,if(jumlahHari=0,sum(harga*kuantitas),sum(harga*kuantitas*jumlahHari))as anggaran " +
                "from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
                "inner join ppmk.rapatKerjaUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.idRapatKerjaUnit " +
                "where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='Y' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rab_rapat.kodePosBiaya " +
                "union all " +
                "select '3' as no, pos2.namaPosBiaya,rab_rapat.kodePosBiaya as kodepos,  " +
                "pos.namaPosBiaya,sum(if(jumlahHari=0,harga*kuantitas,harga*kuantitas*jumlahHari)) " +
                "from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
                "inner join ppmk.kegiatanUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.kodeKegiatanUnit " +
                "inner join ppmk.kelompokKegiatan as klpk on rpt.kodeKelompokKegiatan=klpk.kodeKelompok " +
                "inner join ppmk.kelompokRKA as kplkRKA on klpk.kodeKelompok=kplkRKA.kodeKelompokKegiatan " +
                "inner join ppmk.posBiaya as pos2 on kplkRKA.kodePosBiaya=pos2.kodePosBiaya " +
                "where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='N' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rpt.kodeKelompokKegiatan,rab_rapat.kodePosBiaya " +
                ") as anggaran " +
                "left join " +
                "(select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
                "group by dp.kodePosBiaya " +
                "union all " +
                "select '2'as no,'RAB Rapat Kerja' as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
                "group by dp.kodePosBiaya,dp.kodeSub " +
                "union all " +
                "select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, " +
                "sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
                "inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
                "group by dp.kodePosBiaya,dp.kodeSub " +
                ") as pencairan on anggaran.no=pencairan.no and anggaran.kodepos=pencairan.kodepos " +
                "left join " +
                "(select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as LPJ " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
                "group by dp.kodePosBiaya " +
                "union all " +
                "select '2' as no,'RAB Rapat Kerja' as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
                "group by dp.kodePosBiaya,dp.kodeSubPos " +
                "union all " +
                "select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya,  " +
                "sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
                "inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
                "group by dp.kodePosBiaya,dp.kodeSubPos) lapPJ on anggaran.no=lapPJ.no and anggaran.kodepos=lapPJ.kodepos " +
                "where lapPJ.no is null and pencairan.no is null " +
                "UNION ALL " +
                "select anggaran.no, anggaran.nm, anggaran.kodepos, anggaran.namaPosBiaya, anggaran.anggaran, pencairan.no, pencairan.nm, pencairan.kodepos, pencairan.namaPosBiaya,pencairan.pengajuan,pencairan.cair, " +
                "lapPJ.no, lapPJ.nm, lapPJ.kodepos, lapPJ.namaPosBiaya, lapPJ.pengajuan,lapPJ.LPJ " +
                "from " +
                "(select '1' as no,'RAB Non Rapat' as nm,rab.kodePosBiaya as kodepos,namaPosBiaya,sum(harga*kuantitas)as anggaran " +
                "from ppmk.anggaranRABNonRapatKerja as rab inner join ppmk.posBiaya as pos on rab.kodePosBiaya=pos.kodePosBiaya " +
                "where rab.kodeUnit='"+kdUnker+"' and tahun='"+thn+"' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rab.kodePosBiaya " +
                "union all " +
                "select '2' as no,'RAB Rapat Kerja' as nm,rab_rapat.kodePosBiaya as kodepos, namaPosBiaya,if(jumlahHari=0,sum(harga*kuantitas),sum(harga*kuantitas*jumlahHari))as anggaran " +
                "from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
                "inner join ppmk.rapatKerjaUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.idRapatKerjaUnit " +
                "where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='Y' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rab_rapat.kodePosBiaya " +
                "union all " +
                "select '3' as no, pos2.namaPosBiaya as nm,rab_rapat.kodePosBiaya as kodepos, " +
                "pos.namaPosBiaya,sum(if(jumlahHari=0,harga*kuantitas,harga*kuantitas*jumlahHari)) " +
                "from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
                "inner join ppmk.kegiatanUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.kodeKegiatanUnit " +
                "inner join ppmk.kelompokKegiatan as klpk on rpt.kodeKelompokKegiatan=klpk.kodeKelompok " +
                "inner join ppmk.kelompokRKA as kplkRKA on klpk.kodeKelompok=kplkRKA.kodeKelompokKegiatan " +
                "inner join ppmk.posBiaya as pos2 on kplkRKA.kodePosBiaya=pos2.kodePosBiaya " +
                "where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='N' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rpt.kodeKelompokKegiatan,rab_rapat.kodePosBiaya " +
                ") as anggaran " +
                "right join  " +
                "(select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
                "group by dp.kodePosBiaya " +
                "union all " +
                "select '2'as no,'RAB Rapat Kerja' as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
                "group by dp.kodePosBiaya,dp.kodeSub " +
                "union all " +
                "select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, " +
                "sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
                "inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
                "group by dp.kodePosBiaya,dp.kodeSub " +
                ") as pencairan on anggaran.no=pencairan.no and anggaran.kodepos=pencairan.kodepos " +
                "left join  " +
                "(select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as LPJ " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
                " group by dp.kodePosBiaya " +
                "union all " +
                "select '2' as no,'RAB Rapat Kerja' as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan,  " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
                "group by dp.kodePosBiaya,dp.kodeSubPos " +
                "union all " +
                "select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya,  " +
                "sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                " from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
                " where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
                "group by dp.kodePosBiaya,dp.kodeSubPos) lapPJ on pencairan.no=lapPJ.no and pencairan.kodepos=lapPJ.kodepos " +
                "where lapPJ.no is null and anggaran.no is null " +
                "UNION ALL " +
                "select anggaran.no, anggaran.nm, anggaran.kodepos, anggaran.namaPosBiaya, anggaran.anggaran, " +
                "pencairan.no, pencairan.nm, pencairan.kodepos, pencairan.namaPosBiaya,pencairan.pengajuan,pencairan.cair," +
                "lapPJ.no, lapPJ.nm, lapPJ.kodepos, lapPJ.namaPosBiaya, lapPJ.pengajuan,lapPJ.LPJ " +
                "from " +
                "(select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as LPJ " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
                "group by dp.kodePosBiaya " +
                "union all " +
                "select '2' as no,'RAB Rapat Kerja' as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
                "group by dp.kodePosBiaya,dp.kodeSubPos " +
                "union all " +
                "select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, " +
                "sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
                "sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
                "from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
                "inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
                "inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
                "where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
                "group by dp.kodePosBiaya,dp.kodeSubPos) lapPJ  " +
                "left join " +
                "(select '1' as no,'RAB Non Rapat' as nm,rab.kodePosBiaya as kodepos,namaPosBiaya,sum(harga*kuantitas)as anggaran " +
                "from ppmk.anggaranRABNonRapatKerja as rab inner join ppmk.posBiaya as pos on rab.kodePosBiaya=pos.kodePosBiaya " +
                "where rab.kodeUnit='"+kdUnker+"' and tahun='"+thn+"' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rab.kodePosBiaya " +
                "union all " +
                "select '2' as no,'RAB Rapat Kerja' as nm,rab_rapat.kodePosBiaya as kodepos, namaPosBiaya,if(jumlahHari=0,sum(harga*kuantitas),sum(harga*kuantitas*jumlahHari))as anggaran " +
                "from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
                "inner join ppmk.rapatKerjaUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.idRapatKerjaUnit " +
                "where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='Y' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rab_rapat.kodePosBiaya " +
                "union all " +
                "select '3' as no, pos2.namaPosBiaya as nm,rab_rapat.kodePosBiaya as kodepos,  " +
                "pos.namaPosBiaya,sum(if(jumlahHari=0,harga*kuantitas,harga*kuantitas*jumlahHari)) " +
                "from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
                "inner join ppmk.kegiatanUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.kodeKegiatanUnit" +
                " inner join ppmk.kelompokKegiatan as klpk on rpt.kodeKelompokKegiatan=klpk.kodeKelompok " +
                "inner join ppmk.kelompokRKA as kplkRKA on klpk.kodeKelompok=kplkRKA.kodeKelompokKegiatan " +
                "inner join ppmk.posBiaya as pos2 on kplkRKA.kodePosBiaya=pos2.kodePosBiaya " +
                "where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='N' and isRevisi='Y' and isSudahDiajukan='Y' " +
                "group by rpt.kodeKelompokKegiatan,rab_rapat.kodePosBiaya) " +
                "as anggaran on lapPJ.no=anggaran.no and lapPJ.kodepos=anggaran.kodepos " +
                "left join " +
                "(select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as cair " +
                "from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
                "inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
                "where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
" group by dp.kodePosBiaya " +
" union all " +
" select '2'as no,'RAB Rapat Kerja' as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
" group by dp.kodePosBiaya,dp.kodeSub" +
" union all " +
" select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, " +
" sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
" inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
" group by dp.kodePosBiaya,dp.kodeSub " +
" ) as pencairan on lapPJ.no=pencairan.no and lapPJ.kodepos=pencairan.kodepos " +
" where pencairan.no is null and anggaran.no is null " +
" UNION ALL " +
" select anggaran.no, anggaran.nm, anggaran.kodepos, anggaran.namaPosBiaya, anggaran.anggaran, " +
" pencairan.no, pencairan.nm, pencairan.kodepos, pencairan.namaPosBiaya,pencairan.pengajuan,pencairan.cair, " +
" lapPJ.no, lapPJ.nm, lapPJ.kodepos, lapPJ.namaPosBiaya, lapPJ.pengajuan,lapPJ.LPJ " +
" from " +
" (select '1' as no,'RAB Non Rapat' as nm,rab.kodePosBiaya as kodepos,namaPosBiaya,sum(harga*kuantitas)as anggaran " +
" from ppmk.anggaranRABNonRapatKerja as rab inner join ppmk.posBiaya as pos on rab.kodePosBiaya=pos.kodePosBiaya " +
" where rab.kodeUnit='"+kdUnker+"' and tahun='"+thn+"' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rab.kodePosBiaya " +
" union all " +
" select '2' as no,'RAB Rapat Kerja' as nm,rab_rapat.kodePosBiaya as kodepos, namaPosBiaya,if(jumlahHari=0,sum(harga*kuantitas),sum(harga*kuantitas*jumlahHari))as anggaran " +
" from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
" inner join ppmk.rapatKerjaUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.idRapatKerjaUnit " +
" where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='Y' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rab_rapat.kodePosBiaya " +
"         union all " +
" select '3' as no, pos2.namaPosBiaya,rab_rapat.kodePosBiaya as kodepos, " +
" pos.namaPosBiaya,sum(if(jumlahHari=0,harga*kuantitas,harga*kuantitas*jumlahHari)) " +
" from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
" inner join ppmk.kegiatanUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.kodeKegiatanUnit " +
" inner join ppmk.kelompokKegiatan as klpk on rpt.kodeKelompokKegiatan=klpk.kodeKelompok " +
" inner join ppmk.kelompokRKA as kplkRKA on klpk.kodeKelompok=kplkRKA.kodeKelompokKegiatan " +
" inner join ppmk.posBiaya as pos2 on kplkRKA.kodePosBiaya=pos2.kodePosBiaya " +
" where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='N' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rpt.kodeKelompokKegiatan,rab_rapat.kodePosBiaya " +
" ) as anggaran  " +
" inner join " +
" (select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
" group by dp.kodePosBiaya " +
                " union all "+
" select '2'as no,'RAB Rapat Kerja' as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
" group by dp.kodePosBiaya,dp.kodeSub " +
"         union all " +
" select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, " +
" sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan  " +
" inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
" inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
" group by dp.kodePosBiaya,dp.kodeSub " +
" ) as pencairan on anggaran.no=pencairan.no and anggaran.kodepos=pencairan.kodepos " +
" left join " +
" (select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as LPJ " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
" group by dp.kodePosBiaya " +
        " union all " +
" select '2' as no,'RAB Rapat Kerja' as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
" group by dp.kodePosBiaya,dp.kodeSubPos " +
" union all select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya," +
" sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
" inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
" group by dp.kodePosBiaya,dp.kodeSubPos) lapPJ on anggaran.no=lapPJ.no and anggaran.kodepos=lapPJ.kodepos " +
" where lapPJ.no is null " +
" UNION ALL " +
" select anggaran.no, anggaran.nm, anggaran.kodepos, anggaran.namaPosBiaya, anggaran.anggaran, " +
" pencairan.no, pencairan.nm, pencairan.kodepos, pencairan.namaPosBiaya,pencairan.pengajuan,pencairan.cair, " +
" lapPJ.no, lapPJ.nm, lapPJ.kodepos, lapPJ.namaPosBiaya, lapPJ.pengajuan,lapPJ.LPJ " +
" from " +
" (select '1' as no,'RAB Non Rapat' as nm,rab.kodePosBiaya as kodepos,namaPosBiaya,sum(harga*kuantitas)as anggaran " +
" from ppmk.anggaranRABNonRapatKerja as rab inner join ppmk.posBiaya as pos on rab.kodePosBiaya=pos.kodePosBiaya " +
" where rab.kodeUnit='"+kdUnker+"' and tahun='"+thn+"' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rab.kodePosBiaya " +
" union all " +
" select '2' as no,'RAB Rapat Kerja' as nm,rab_rapat.kodePosBiaya as kodepos, namaPosBiaya,if(jumlahHari=0,sum(harga*kuantitas),sum(harga*kuantitas*jumlahHari))as anggaran " +
" from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
" inner join ppmk.rapatKerjaUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.idRapatKerjaUnit " +
" where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='Y' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rab_rapat.kodePosBiaya " +
" union all " +
" select '3' as no, pos2.namaPosBiaya,rab_rapat.kodePosBiaya as kodepos, " +
" pos.namaPosBiaya,sum(if(jumlahHari=0,harga*kuantitas,harga*kuantitas*jumlahHari)) " +
" from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
" inner join ppmk.kegiatanUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.kodeKegiatanUnit " +
" inner join ppmk.kelompokKegiatan as klpk on rpt.kodeKelompokKegiatan=klpk.kodeKelompok " +
" inner join ppmk.kelompokRKA as kplkRKA on klpk.kodeKelompok=kplkRKA.kodeKelompokKegiatan " +
" inner join ppmk.posBiaya as pos2 on kplkRKA.kodePosBiaya=pos2.kodePosBiaya " +
" where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='N' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rpt.kodeKelompokKegiatan,rab_rapat.kodePosBiaya " +
" ) as anggaran " +
" right join " +
" (select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
" group by dp.kodePosBiaya " +
" union all " +
" select '2'as no,'RAB Rapat Kerja' as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
" group by dp.kodePosBiaya,dp.kodeSub " +
" union all " +
" select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, " +
" sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
" inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
" group by dp.kodePosBiaya,dp.kodeSub " +
" ) as pencairan on anggaran.no=pencairan.no and anggaran.kodepos=pencairan.kodepos " +
" inner join " +
" (select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as LPJ " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
" group by dp.kodePosBiaya " +
" union all " +
" select '2' as no,'RAB Rapat Kerja' as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
" group by dp.kodePosBiaya,dp.kodeSubPos " +
" union all " +
" select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, " +
" sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
" inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
" group by dp.kodePosBiaya,dp.kodeSubPos) lapPJ on pencairan.no=lapPJ.no and pencairan.kodepos=lapPJ.kodepos " +
" where anggaran.no is null " +
" UNION ALL " +
" select anggaran.no, anggaran.nm, anggaran.kodepos, anggaran.namaPosBiaya, anggaran.anggaran, " +
" pencairan.no, pencairan.nm, pencairan.kodepos, pencairan.namaPosBiaya,pencairan.pengajuan,pencairan.cair, " +
" lapPJ.no, lapPJ.nm, lapPJ.kodepos, lapPJ.namaPosBiaya, lapPJ.pengajuan,lapPJ.LPJ " +
" from " +
" (select '1' as no,'RAB Non Rapat' as nm,rab.kodePosBiaya as kodepos,namaPosBiaya,sum(harga*kuantitas)as anggaran " +
" from ppmk.anggaranRABNonRapatKerja as rab inner join ppmk.posBiaya as pos on rab.kodePosBiaya=pos.kodePosBiaya " +
" where rab.kodeUnit='"+kdUnker+"' and tahun='"+thn+"' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rab.kodePosBiaya " +
" union all " +
" select '2' as no,'RAB Rapat Kerja' as nm,rab_rapat.kodePosBiaya as kodepos, namaPosBiaya,if(jumlahHari=0,sum(harga*kuantitas),sum(harga*kuantitas*jumlahHari))as anggaran " +
" from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
" inner join ppmk.rapatKerjaUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.idRapatKerjaUnit " +
" where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='Y' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rab_rapat.kodePosBiaya " +
" union all " +
" select '3' as no, pos2.namaPosBiaya,rab_rapat.kodePosBiaya as kodepos, " +
" pos.namaPosBiaya,sum(if(jumlahHari=0,harga*kuantitas,harga*kuantitas*jumlahHari)) " +
" from ppmk.anggaranBiayaRapatKerjaDanKegiatan as rab_rapat inner join ppmk.posBiaya as pos on rab_rapat.kodePosBiaya=pos.kodePosBiaya " +
" inner join ppmk.kegiatanUnit as rpt on rab_rapat.kodeRapatKerjaSubKegiatan=rpt.kodeKegiatanUnit " +
" inner join ppmk.kelompokKegiatan as klpk on rpt.kodeKelompokKegiatan=klpk.kodeKelompok " +
" inner join ppmk.kelompokRKA as kplkRKA on klpk.kodeKelompok=kplkRKA.kodeKelompokKegiatan " +
" inner join ppmk.posBiaya as pos2 on kplkRKA.kodePosBiaya=pos2.kodePosBiaya " +
" where rpt.kodeUnit='"+kdUnker+"' and rpt.tahun='"+thn+"' and isRapat='N' and isRevisi='Y' and isSudahDiajukan='Y' " +
" group by rpt.kodeKelompokKegiatan,rab_rapat.kodePosBiaya " +
" ) as anggaran " +
" inner join " +
" (select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as LPJ " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
" group by dp.kodePosBiaya " +
" union all " +
" select '2' as no,'RAB Rapat Kerja' as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
" group by dp.kodePosBiaya,dp.kodeSubPos " +
" union all " +
" select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSubPos as kodepos,pos.namaPosBiaya, " +
" sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair," +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.lpj as pp inner join ppmk.detailPosLPJ as dp on pp.idLPJ=dp.idLPJ " +
" inner join ppmk.posBiaya as pos on dp.kodeSubPos=pos.kodePosbiaya " +
" inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
" where pp.tahun='"+thn+"' and pp.unit='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
" group by dp.kodePosBiaya,dp.kodeSubPos) lapPJ  on anggaran.no=lapPJ.no and anggaran.kodepos=lapPJ.kodepos " +
" left join " +
" (select '1' as no,'RAB Non Rapat' as nm,dp.kodePosBiaya as kodepos,pos.namaPosBiaya, sum(harga*kuantitasPengajuan) as pengajuan, sum(harga*kuantitasDisetujui) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodePosBiaya=pos.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya<>5 " +
" group by dp.kodePosBiaya " +
" union all " +
" select '2'as no,'RAB Rapat Kerja' as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as pengajuan, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='1' and dp.kodePosBiaya=5 " +
" group by dp.kodePosBiaya,dp.kodeSub " +
" union all " +
" select '3' as no,pos2.namaPosBiaya as nm,dp.kodeSub as kodepos,pos.namaPosBiaya, " +
" sum(if(jumlahHariPengajuan=0,(harga*kuantitasPengajuan),(harga*kuantitasPengajuan*jumlahHariPengajuan))) as cair, " +
" sum(if(jumlahHariDisetujui=0,(harga*kuantitasDisetujui),(harga*kuantitasDisetujui*jumlahHariDisetujui))) as cair " +
" from ppmk.pengajuanPengambilan as pp inner join ppmk.detailPengajuanPengambilan as dp on pp.idPengajuan=dp.idPengajuan " +
" inner join ppmk.posBiaya as pos on dp.kodeSub=pos.kodePosbiaya " +
" inner join ppmk.posBiaya as pos2 on dp.kodePosBiaya=pos2.kodePosbiaya " +
" where pp.tahunAnggaran='"+thn+"' and pp.kodeUnitPengaju='"+kdUnker+"' and dp.kodeJenisAnggaran='2' " +
" group by dp.kodePosBiaya,dp.kodeSub " +
" ) as pencairan on anggaran.no=pencairan.no and anggaran.kodepos=pencairan.kodepos " +
" where pencairan.no is null) as subRekap order by no1,namaPosBiaya1 ";
        //Messagebox.show(xStr);
        return ClassConnection.getJdbc().query(xStr, new DataPengajuanPencairanDana());
    }

    protected class DataPengajuanPencairanDana implements RowMapper {

        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            RekapAll xtemp = new RekapAll();
            xtemp.setA(rs.getString("nm1"));
            xtemp.setB(rs.getString("namaPosBiaya1"));
            xtemp.setC(rs.getBigDecimal("anggaran1"));
            xtemp.setD(rs.getString("nm2"));
            xtemp.setE(rs.getString("namaPosBiaya2"));
            xtemp.setF(rs.getBigDecimal("pengajuan2"));
            xtemp.setG(rs.getBigDecimal("cair2"));
            xtemp.setH(rs.getString("nm3"));
            xtemp.setI(rs.getString("namaPosBiaya3"));
            xtemp.setJ(rs.getBigDecimal("pengajuan3"));
            xtemp.setK(rs.getBigDecimal("LPJ3"));

//            xtemp.setJenisBiaya(rs.getString("jenisbiaya"));
//            xtemp.setKegiatanRapat(rs.getString("kegiatanrapat"));
//            xtemp.setPosBiaya(rs.getString("posbiaya"));
//            xtemp.setSubPosBiaya(rs.getString("subposbiaya"));
//            xtemp.setItemBiaya(rs.getString("itembiaya"));
//            xtemp.setSubItemBiaya(rs.getString("subitembiaya"));
//            xtemp.setTempHarga(rs.getString("harga"));
//            xtemp.setTempKuantitasDisetujui(rs.getString("kuantitas"));
//            xtemp.setJumlahHariDisetujui(rs.getString("jumlahhari"));
//            xtemp.setNominal(rs.getString("nominal"));
            return xtemp;
        }
    }

    public LinkedList getFieldLpj() throws Exception {
        LinkedList xls = new LinkedList();
        xls.add("nm1");
        xls.add("namaPosBiaya1");
        xls.add("anggaran1");
        xls.add("nm2");
        xls.add("namaPosBiaya2");
        xls.add("pengajuan2");
        xls.add("cair2");
        xls.add("nm3");
        xls.add("namaPosBiaya3");
        xls.add("pengajuan3");
        xls.add("LPJ3");
        return xls;
    }

    public Object[][] DataLpjToCetak(String kdUnker, String thn) throws Exception {
        LinkedList xFieldPengajuanPencairanDana = getFieldLpj();
        List xlsPengajuanPencairanDana = getLpj(kdUnker, thn);
        Object[][] xBufResult = new Object[xlsPengajuanPencairanDana.size()][xFieldPengajuanPencairanDana.size()];
        int i = 0;
        for (Iterator it = xlsPengajuanPencairanDana.iterator(); it.hasNext();) {
            RekapAll xPengajuanPencairanDana = (RekapAll) it.next();
            xBufResult[i][0] = xPengajuanPencairanDana.getA();
            xBufResult[i][1] = xPengajuanPencairanDana.getB();
            xBufResult[i][2] = xPengajuanPencairanDana.getC();
            xBufResult[i][3] = xPengajuanPencairanDana.getD();
            xBufResult[i][4] = xPengajuanPencairanDana.getE();
            xBufResult[i][5] = xPengajuanPencairanDana.getF();
            xBufResult[i][6] = xPengajuanPencairanDana.getG();
            xBufResult[i][7] = xPengajuanPencairanDana.getH();
            xBufResult[i][8] = xPengajuanPencairanDana.getI();
            xBufResult[i][9] = xPengajuanPencairanDana.getJ();
            xBufResult[i][10] = xPengajuanPencairanDana.getK();
            i++;
        }
        return xBufResult;
    }

}