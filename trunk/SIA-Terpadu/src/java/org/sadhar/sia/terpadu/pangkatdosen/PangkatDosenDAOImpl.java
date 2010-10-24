/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.pangkatdosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class PangkatDosenDAOImpl implements PangkatDosenDAO {

    static {
        String sql = "CREATE OR REPLACE VIEW tempo.pangkatdosen(npp,Nama_peg,umur,tgl_sk,kd_unit,Nama_unit_kerja, "
                + " kode_pang ) AS "
                + " SELECT up.npp,pp.Nama_peg,year(CURDATE()) - year(pp.Tgl_lahir) as umur,MAX(up.tgl_sk_unit)as tgl_sk,up.kd_unit,uk.Nama_unit_kerja, "
                + " MAX(cast(ppp.Kd_pang as UNSIGNED)) as kode_pang "
                + " from personalia.unit_peg up "
                + " LEFT OUTER JOIN kamus.unkerja uk ON (up.kd_unit = uk.Kd_unit_kerja ) "
                + " INNER JOIN personalia.pegawai pp ON (up.NPP = pp.npp and pp.AdmEdu = '2') "
                + " INNer JOIN personalia.pangkat_peg ppp ON (up.npp=ppp.NPP ) "
                + " INNER JOIN kamus.pangkat kp ON (kp.Kd_pang = ppp.Kd_pang) "
                + " group by up.npp  order by up.npp";

        ClassConnection.getJdbc().queryForInt(sql);
    }

    public PangkatDosenDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<PangkatDosen> gets() throws Exception {
        String sql = "select pd.npp as npp,pd.Nama_peg as nama,pd.umur as umur, "
                + " pd.tgl_sk as tanggalsk,pd.kd_unit as kodeunit,pd.Nama_unit_kerja as namaunit,"
                + " pd.kode_pang as kodepangkat,  kp.Nama_pang as namapangkat from tempo.pangkatdosen pd"
                + " inner join kamus.pangkat kp on (pd.kode_pang=kp.Kd_pang)";
        List<PangkatDosen> result = new ArrayList<PangkatDosen>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            PangkatDosen pd = new PangkatDosen();
            pd.setNpp(ClassAntiNull.AntiNullString(m.get("npp")));
            pd.setNama(ClassAntiNull.AntiNullString(m.get("nama")));
            pd.setUmur(ClassAntiNull.AntiNullInt(m.get("umur")));
            pd.setTanggalSK(ClassAntiNull.AntiNullString(m.get("tanggalsk")));
            pd.setKodeUnitKerja(ClassAntiNull.AntiNullString(m.get("kodeunit")));
            pd.setNamaUnitKerja(ClassAntiNull.AntiNullString(m.get("namaunit")));
            pd.setKodePangkat(ClassAntiNull.AntiNullString(m.get("kodepangkat")));
            pd.setNamaPangkat(ClassAntiNull.AntiNullString(m.get("namapangkat")));

            result.add(pd);


        }
        return result;
    }
}
