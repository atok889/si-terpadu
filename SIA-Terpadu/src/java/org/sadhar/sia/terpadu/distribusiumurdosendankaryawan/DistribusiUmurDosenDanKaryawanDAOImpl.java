/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.distribusiumurdosendankaryawan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class DistribusiUmurDosenDanKaryawanDAOImpl implements DistribusiUmurDosenDanKaryawanDAO {

    public DistribusiUmurDosenDanKaryawanDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT SUBSTRING(ku.Kd_unit_kerja,4,4) as shortKode, ku.Kd_unit_kerja as kodeUnitKerja," +
                " ku.Nama_unit_kerja as nama FROM kamus.unkerja ku WHERE ku.Nama_unit_kerja like 'PROGRAM STUDI%'";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getAll() {
        String sql = "SELECT " +
                " pegawai.Nama_peg as nama, " +
                " golgaji.Nama_gol as golongan, " +
                " pangkat.Nama_pang as jabatan, " +
                " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) as umur," +
                " 'karyawan' as status " +
                " FROM personalia.golongan_peg golongan_peg " +
                " INNER JOIN personalia.pegawai pegawai ON (golongan_peg.kdPegawai = pegawai.kdPegawai)" +
                " INNER JOIN kamus.golgaji golgaji ON (golongan_peg.Kd_gol = golgaji.Kd_gol)" +
                " INNER JOIN personalia.pangkat_peg pangkat_peg ON (pangkat_peg.kdPegawai = pegawai.kdPegawai)" +
                " INNER JOIN kamus.pangkat pangkat ON (pangkat_peg.Kd_pang = pangkat.Kd_pang)" +
                " INNER JOIN kamus.unit_peg unit_peg ON (unit_peg.NPP = pegawai.NPP)" +
                " WHERE pegawai.Status_keluar= '1' AND pegawai.AdmEdu!='2'" +
                " UNION" +
                " SELECT " +
                " pegawai.Nama_peg as nama," +
                " golgaji.Nama_gol as golongan," +
                " jab_akad.Nama_jab_akad as jabatan," +
                " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) as umur," +
                " 'dosen' as status " +
                " FROM personalia.golongan_peg golongan_peg " +
                " INNER JOIN personalia.pegawai pegawai ON (golongan_peg.kdPegawai = pegawai.kdPegawai)" +
                " INNER JOIN kamus.golgaji golgaji ON (golongan_peg.Kd_gol = golgaji.Kd_gol) " +
                " INNER JOIN personalia.jab_akad_pegawai jab_akad_pegawai ON (jab_akad_pegawai.kdPegawai = pegawai.kdPegawai)" +
                " INNER JOIN kamus.jab_akad jab_akad ON (jab_akad_pegawai.Kd_Jabak = jab_akad.Kd_jab_akad)" +
                " INNER JOIN kamus.unit_peg unit_peg ON (unit_peg.NPP = pegawai.NPP) " +
                " WHERE pegawai.Status_keluar= '1' AND pegawai.AdmEdu ='2'  " +
                " GROUP BY pegawai.kdPegawai ORDER BY umur,nama";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getAll(String kodeUnit) {
        String sql = "SELECT " +
                " pegawai.Nama_peg as nama, " +
                " golgaji.Nama_gol as golongan, " +
                " pangkat.Nama_pang as jabatan, " +
                " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) as umur," +
                " 'karyawan' as status " +
                " FROM personalia.golongan_peg golongan_peg " +
                " INNER JOIN personalia.pegawai pegawai ON (golongan_peg.kdPegawai = pegawai.kdPegawai)" +
                " INNER JOIN kamus.golgaji golgaji ON (golongan_peg.Kd_gol = golgaji.Kd_gol)" +
                " INNER JOIN personalia.pangkat_peg pangkat_peg ON (pangkat_peg.kdPegawai = pegawai.kdPegawai)" +
                " INNER JOIN kamus.pangkat pangkat ON (pangkat_peg.Kd_pang = pangkat.Kd_pang)" +
                " INNER JOIN kamus.unit_peg unit_peg ON (unit_peg.NPP = pegawai.NPP)" +
                " WHERE pegawai.Status_keluar= '1' AND pegawai.AdmEdu!='2' AND unit_peg.Kd_unit='" + kodeUnit + "'" +
                " UNION" +
                " SELECT " +
                " pegawai.Nama_peg as nama," +
                " golgaji.Nama_gol as golongan," +
                " jab_akad.Nama_jab_akad as jabatan," +
                " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) as umur," +
                " 'dosen' as status " +
                " FROM personalia.golongan_peg golongan_peg " +
                " INNER JOIN personalia.pegawai pegawai ON (golongan_peg.kdPegawai = pegawai.kdPegawai)" +
                " INNER JOIN kamus.golgaji golgaji ON (golongan_peg.Kd_gol = golgaji.Kd_gol) " +
                " INNER JOIN personalia.jab_akad_pegawai jab_akad_pegawai ON (jab_akad_pegawai.kdPegawai = pegawai.kdPegawai)" +
                " INNER JOIN kamus.jab_akad jab_akad ON (jab_akad_pegawai.Kd_Jabak = jab_akad.Kd_jab_akad)" +
                " INNER JOIN kamus.unit_peg unit_peg ON (unit_peg.NPP = pegawai.NPP) " +
                " WHERE pegawai.Status_keluar= '1' AND pegawai.AdmEdu ='2' AND unit_peg.Kd_unit='" + kodeUnit + "'" +
                " GROUP BY pegawai.kdPegawai ORDER BY umur,nama";
        return ClassConnection.getJdbc().queryForList(sql);
    }

    public List<Map> getAll(String kodeProdi, String min, String max) {
        List<Map> results = new ArrayList<Map>();
        if (kodeProdi == null) {
            String sql = "SELECT " +
                    " pegawai.Nama_peg as nama, " +
                    " golgaji.Nama_gol as golongan, " +
                    " pangkat.Nama_pang as jabatan, " +
                    " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) as umur," +
                    " 'karyawan' as status " +
                    " FROM personalia.golongan_peg golongan_peg " +
                    " INNER JOIN personalia.pegawai pegawai ON (golongan_peg.kdPegawai = pegawai.kdPegawai)" +
                    " INNER JOIN kamus.golgaji golgaji ON (golongan_peg.Kd_gol = golgaji.Kd_gol)" +
                    " INNER JOIN personalia.pangkat_peg pangkat_peg ON (pangkat_peg.kdPegawai = pegawai.kdPegawai)" +
                    " INNER JOIN kamus.pangkat pangkat ON (pangkat_peg.Kd_pang = pangkat.Kd_pang)" +
                    " INNER JOIN kamus.unit_peg unit_peg ON (unit_peg.NPP = pegawai.NPP)" +
                    " WHERE pegawai.Status_keluar= '1' AND pegawai.AdmEdu !='2' AND " +
                    " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) >= " + min + " AND TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) <= " + max + "" +
                    " UNION " +
                    " SELECT " +
                    " pegawai.Nama_peg as nama," +
                    " golgaji.Nama_gol as golongan," +
                    " jab_akad.Nama_jab_akad as jabatan," +
                    " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) as umur," +
                    " 'dosen' as status " +
                    " FROM personalia.golongan_peg golongan_peg " +
                    " INNER JOIN personalia.pegawai pegawai ON (golongan_peg.kdPegawai = pegawai.kdPegawai)" +
                    " INNER JOIN kamus.golgaji golgaji ON (golongan_peg.Kd_gol = golgaji.Kd_gol) " +
                    " INNER JOIN personalia.jab_akad_pegawai jab_akad_pegawai ON (jab_akad_pegawai.kdPegawai = pegawai.kdPegawai)" +
                    " INNER JOIN kamus.jab_akad jab_akad ON (jab_akad_pegawai.Kd_Jabak = jab_akad.Kd_jab_akad)" +
                    " INNER JOIN kamus.unit_peg unit_peg ON (unit_peg.NPP = pegawai.NPP) " +
                    " WHERE pegawai.Status_keluar= '1' AND pegawai.AdmEdu ='2' AND " +
                    " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) >= " + min + " AND TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) <= " + max + "" +
                    " GROUP BY pegawai.kdPegawai ORDER BY umur,nama";
            results.addAll(ClassConnection.getJdbc().queryForList(sql));
        } else {
            String sql = "SELECT " +
                    " pegawai.Nama_peg as nama, " +
                    " golgaji.Nama_gol as golongan, " +
                    " pangkat.Nama_pang as jabatan, " +
                    " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) as umur," +
                    " 'karyawan' as status " +
                    " FROM personalia.golongan_peg golongan_peg " +
                    " INNER JOIN personalia.pegawai pegawai ON (golongan_peg.kdPegawai = pegawai.kdPegawai)" +
                    " INNER JOIN kamus.golgaji golgaji ON (golongan_peg.Kd_gol = golgaji.Kd_gol)" +
                    " INNER JOIN personalia.pangkat_peg pangkat_peg ON (pangkat_peg.kdPegawai = pegawai.kdPegawai)" +
                    " INNER JOIN kamus.pangkat pangkat ON (pangkat_peg.Kd_pang = pangkat.Kd_pang)" +
                    " INNER JOIN kamus.unit_peg unit_peg ON (unit_peg.NPP = pegawai.NPP)" +
                    " WHERE pegawai.Status_keluar= '1' AND pegawai.AdmEdu !='2' AND unit_peg.Kd_unit='" + kodeProdi + "' AND " +
                    " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) >= " + min + " AND TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) <= " + max + "" +
                    " UNION " +
                    " SELECT " +
                    " pegawai.Nama_peg as nama," +
                    " golgaji.Nama_gol as golongan," +
                    " jab_akad.Nama_jab_akad as jabatan," +
                    " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) as umur," +
                    " 'dosen' as status " +
                    " FROM personalia.golongan_peg golongan_peg " +
                    " INNER JOIN personalia.pegawai pegawai ON (golongan_peg.kdPegawai = pegawai.kdPegawai)" +
                    " INNER JOIN kamus.golgaji golgaji ON (golongan_peg.Kd_gol = golgaji.Kd_gol) " +
                    " INNER JOIN personalia.jab_akad_pegawai jab_akad_pegawai ON (jab_akad_pegawai.kdPegawai = pegawai.kdPegawai)" +
                    " INNER JOIN kamus.jab_akad jab_akad ON (jab_akad_pegawai.Kd_Jabak = jab_akad.Kd_jab_akad)" +
                    " INNER JOIN kamus.unit_peg unit_peg ON (unit_peg.NPP = pegawai.NPP) " +
                    " WHERE pegawai.Status_keluar= '1' AND pegawai.AdmEdu ='2' AND unit_peg.Kd_unit='" + kodeProdi + "' AND " +
                    " TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) >= " + min + " AND TIMESTAMPDIFF(YEAR,pegawai.Tgl_lahir, DATE(now())) <= " + max + "" +
                    " GROUP BY pegawai.kdPegawai ORDER BY umur,nama";
            results.addAll(ClassConnection.getJdbc().queryForList(sql));
        }

        return results;
    }
}
