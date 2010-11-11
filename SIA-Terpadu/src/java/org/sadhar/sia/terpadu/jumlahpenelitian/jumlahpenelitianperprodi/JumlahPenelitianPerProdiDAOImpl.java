/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jumlahpenelitian.jumlahpenelitianperprodi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author kris
 */
public class JumlahPenelitianPerProdiDAOImpl implements JumlahPenelitianPerProdiDAO {

    public JumlahPenelitianPerProdiDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<Map> getProdi() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getUnitKerja() {
        String sql = "SELECT Kd_prg, Nama_prg FROM kamus.prg_std WHERE Kd_prg != '0000' ORDER BY Kd_prg";
        List<Map> maps = ClassConnection.getJdbc().queryForList(sql);
        return maps;
    }

    public List<Map> getJumlahPenelitianPerProdi() {
        List<Map> resutls = new ArrayList<Map>();
        for (int i = new DateTime().getYear() - 5; i <= new DateTime().getYear(); i++) {
            String sql = "select count(*) as jumlah,ku.Nama_unit_kerja,d.tahunPenilaian,tglPelaksanaan as waktu " +
                    " from personalia.detailPenilaianAngkaKredit as d " +
                    " inner join personalia.butirKegiatanPenilaian as b on d.idButir=b.idButir" +
                    " inner join personalia.subUnsur as s on b.idSubUnsur=s.idSubUnsur " +
                    " inner join personalia.pegawai as pp on pp.kdPegawai = d.kdPegawai " +
                    " inner join personalia.unit_peg as pu on pu.kdPegawai=d.kdPegawai " +
                    " inner join kamus.unkerja as ku on ku.Kd_unit_kerja=pu.kd_unit " +
                    " where s.kodeKomponen=3 and  d.tahunPenilaian='" + i + "'" +
                    " group by ku.Nama_unit_kerja  " +
                    " order by ku.Nama_unit_kerja";

            resutls.addAll(ClassConnection.getJdbc().queryForList(sql));
        }
        return resutls;
    }
}
