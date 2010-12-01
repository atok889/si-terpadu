/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarkaryawandandosenyangakanpensiun;

import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class DaftarKaryawanDanDosenYangAkanPensiunDAOImpl implements DaftarKaryawanDanDosenYangAkanPensiunDAO {

    public DaftarKaryawanDanDosenYangAkanPensiunDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getDaftarDosenDanKaryawanYangAkanPensiun(String tahun) {
//        String sqlCreateView = "CREATE OR REPLACE VIEW kamus.pensiun(nama,unit_kerja,umur,pensiun) AS " +
//                " SELECT " +
//                " concat_ws(' ',Gelar_depan,`Nama_peg`,`Gelar_blk`) as Nama_peg, " +
//                " unkerja.Nama_unit_kerja, " +
//                " CAST(TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) AS CHAR) as umur, " +
//                " IF(SUBSTRING(pegawai.Gelar_depan,1,4)='Prof', " +
//                " IF(jenjang.Nm_jenjang = 'S3',CAST(70-TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) AS CHAR ),''), " +
//                " IF(jenjang.Nm_jenjang = 'S1',CAST(55-TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) AS CHAR ), " +
//                " IF(jenjang.Nm_jenjang = 'S2',CAST(60-TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) AS CHAR ), " +
//                " IF(jenjang.Nm_jenjang = 'S3',CAST(60-TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) AS CHAR ),'')))) as pensiun " +
//                " FROM personalia.pegawai pegawai " +
//                " INNER JOIN personalia.pendidikan pendidikan ON (pegawai.kdPegawai = pendidikan.kdPegawai) " +
//                " INNER JOIN kamus.unit_peg unit_peg ON (pegawai.NPP = unit_peg.NPP) " +
//                " INNER JOIN kamus.unkerja unkerja ON (unit_peg.Kd_unit = unkerja.Kd_unit_kerja) " +
//                " INNER JOIN kamus.jenjang jenjang ON (jenjang.Kd_jenjang = pendidikan.Jenjang) " +
//                " WHERE (pegawai.Status_keluar = '1' AND pegawai.stat_peg='1') " +
//                " GROUP BY pegawai.Nama_peg";

        String sqlCreateView1 = "CREATE OR REPLACE VIEW tempo.pensiun AS " +
                " SELECT  pegawai.Gelar_depan,pegawai.Tgl_lahir, CONCAT_WS(' ',Gelar_depan,`Nama_peg`,`Gelar_blk`) as Nama_peg, unkerja.Nama_unit_kerja," +
                " CAST(TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) AS CHAR) as umur, " +
                " jenjang.Nm_jenjang,max(jenjang.Kd_jenjang) as Kd_jenjang  " +
                " FROM personalia.pegawai pegawai " +
                " INNER JOIN personalia.pendidikan pendidikan ON (pegawai.kdPegawai = pendidikan.kdPegawai) " +
                " INNER JOIN kamus.unit_peg unit_peg ON (pegawai.NPP = unit_peg.NPP) " +
                " INNER JOIN kamus.unkerja unkerja ON (unit_peg.Kd_unit = unkerja.Kd_unit_kerja)  " +
                " INNER JOIN kamus.jenjang jenjang ON (jenjang.Kd_jenjang = pendidikan.Jenjang) " +
                " WHERE (pegawai.Status_keluar = '1' AND pegawai.stat_peg='1') GROUP BY pegawai.Nama_peg";

        ClassConnection.getJdbc().execute(sqlCreateView1);

        String sqlCreateView2 = "CREATE OR REPLACE VIEW tempo.pensiun_vi AS " +
                " SELECT Nama_peg,jenjang.Nm_jenjang,kp.Kd_jenjang,Nama_unit_kerja,umur, " +
                " IF(SUBSTRING(kp.Gelar_depan,1,4)='Prof', " +
                " IF(jenjang.Nm_jenjang = 'S3',CAST(70-TIMESTAMPDIFF(YEAR,kp.Tgl_lahir, DATE(now())) AS CHAR ),''), " +
                " IF(jenjang.Nm_jenjang = 'S1',CAST(55-TIMESTAMPDIFF(YEAR,kp.Tgl_lahir, DATE(now())) AS CHAR ), " +
                " IF(jenjang.Nm_jenjang = 'S2',CAST(60-TIMESTAMPDIFF(YEAR,kp.Tgl_lahir, DATE(now())) AS CHAR ), " +
                " IF(jenjang.Nm_jenjang = 'S3',CAST(70-TIMESTAMPDIFF(YEAR,kp.Tgl_lahir, DATE(now())) AS CHAR ),'')))) as pensiun " +
                " FROM tempo.pensiun kp INNER JOIN kamus.jenjang jenjang ON jenjang.Kd_jenjang = kp.Kd_jenjang";

        ClassConnection.getJdbc().execute(sqlCreateView2);

        String sql = "SELECT  Nama_peg as nama, Nama_unit_kerja as unit_kerja, pensiun , Nm_jenjang, umur  FROM tempo.pensiun_vi WHERE pensiun BETWEEN 1 AND " + tahun;
        return ClassConnection.getJdbc().queryForList(sql);
    }
}
