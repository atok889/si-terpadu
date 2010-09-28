/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sadhar.sia.terpadu.daftarmatakuliah;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sadhar.errhandler.ClassAntiNull;
import org.sadhar.sia.common.ClassConnection;
import org.sadhar.sia.terpadu.prodi.ProgramStudi;

/**
 *
 * @author Hendro Steven
 */
public class DaftarMatakuliahDAOImpl implements DaftarMatakuliahDAO {

    public DaftarMatakuliahDAOImpl() {
        ClassConnection.getTransactionProxyFactoryBean().setTarget(this);
    }

    public List<DaftarMatakuliah> getBeanCollection(ProgramStudi prodi) throws Exception {
        List<DaftarMatakuliah> data = new ArrayList<DaftarMatakuliah>();
        String sql = "SELECT db_" + prodi.getKode() + ".mtk" + prodi.getKode() + ".kd_mtk as kode,"
                + "db_" + prodi.getKode() + ".mtk" + prodi.getKode() + ".nama_mtk as nama,"
                + "db_" + prodi.getKode() + ".mtk" + prodi.getKode() + ".SKS as sks,personalia.pegawai.Nama_peg as dosen "
                + "FROM personalia.pegawai pegawai "
                + "RIGHT JOIN db_" + prodi.getKode() + ".mtk" + prodi.getKode() + " mtk" + prodi.getKode() + " "
                + "ON (pegawai.kdPegawai = mtk" + prodi.getKode() + ".DsnKoordinator)";
        List<Map> rows = ClassConnection.getJdbc().queryForList(sql);
        int nomor = 1;
        for (Map m : rows) {
            DaftarMatakuliah dm = new DaftarMatakuliah();
            dm.setNomor(nomor++);
            dm.setKode(m.get("kode").toString());
            dm.setNama(m.get("nama").toString());
            dm.setSks(Integer.valueOf(m.get("sks").toString()));
            dm.setDosen(ClassAntiNull.AntiNullString(m.get("dosen")));
            data.add(dm);
        }
        return data;
    }
}
