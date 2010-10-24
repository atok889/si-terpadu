/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.jabatanakademikdosen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;

/**
 *
 * @author jasoet
 */
public class JabatanAkademikDosenDAOImpl implements JabatanAkademikDosenDAO {

    static {
        String sql = "CREATE OR REPLACE VIEW tempo.jabatandosen(NPP,Nama_peg,umur, "
                + " `kd_unit`,`nama_unit_kerja`, Kd_jabak  ) AS "
                + " (select pjap.`NPP`, pp.`Nama_peg`, year(CURDATE()) - year(pp.Tgl_lahir) as umur, "
                + " ppu.`kd_unit`,ku.`nama_unit_kerja`, "
                + "  MAX(CAST(pjap.Kd_Jabak as UNSIGNED)) AS Kd_jabak "
                + " from personalia.jab_akad_pegawai pjap "
                + " INNER JOIN personalia.unit_peg ppu on pjap.`NPP`=ppu.`npp` "
                + " INNER JOIN personalia.pegawai pp on pjap.`NPP`=pp.`NPP` "
                + " LEFT OUTER JOIN kamus.unkerja ku on ku.`kd_unit_kerja`=ppu.`kd_unit` "
                + " INNER JOIN kamus.jab_akad kja on kja.`kd_jab_akad` =pjap.`Kd_jabak` "
                + " group by pjap.npp  order by pjap.npp ) ";

        ClassConnection.getJdbc().queryForInt(sql);
    }

    public JabatanAkademikDosenDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<JabatanAkademikDosen> gets() throws Exception {
        String sql = "select NPP as npp,Nama_peg as nama,umur as umur, "
                + "  `kd_unit` as kodeunit,`nama_unit_kerja` as namaunit, "
                + " Kd_jabak as kodejabatan , kja.Nama_jab_akad as namajabatan "
                + " From tempo.jabatandosen tjd "
                + "  INNER JOIN kamus.jab_akad kja on (tjd.Kd_jabak=kja.Kd_jab_akad)";
        List<JabatanAkademikDosen> result = new ArrayList<JabatanAkademikDosen>();
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        for (Map m : rows) {
            JabatanAkademikDosen pd = new JabatanAkademikDosen();
            pd.setNpp(ClassAntiNull.AntiNullString(m.get("npp")));
            pd.setNama(ClassAntiNull.AntiNullString(m.get("nama")));
            pd.setUmur(ClassAntiNull.AntiNullInt(m.get("umur")));
            pd.setKodeUnitKerja(ClassAntiNull.AntiNullString(m.get("kodeunit")));
            pd.setNamaUnitKerja(ClassAntiNull.AntiNullString(m.get("namaunit")));
            pd.setKodeJabatan(ClassAntiNull.AntiNullString(m.get("kodejabatan")));
            pd.setNamaJabatan(ClassAntiNull.AntiNullString(m.get("namajabatan")));


            result.add(pd);


        }
        return result;
    }
}
