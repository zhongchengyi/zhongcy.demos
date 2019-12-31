package zhongcy.demos;

import dev.morphia.query.Query;
import zhongcy.demos.model.MorphiaDemoModel;
import zhongcy.demos.model.SchoolClassLevel;
import zhongcy.demos.model.SchoolClassLevel1;

import java.io.Console;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            MorphiaDemoModel model = new MorphiaDemoModel();
            model.schoolClassLevel = SchoolClassLevel.KINDERGARTEN_LARGE;
            model.schoolClassLevel1 = SchoolClassLevel1.KINDERGARTEN_MIDDLE;
            MdManager.instance.getHyDatastroe().save(model);

            Query<MorphiaDemoModel> query = MdManager.instance.getHyDatastroe().createQuery(MorphiaDemoModel.class);
            List<MorphiaDemoModel> mds = query.filter("_id", model.id).find().toList();

            int i = 0;
        } catch (Exception e) {

        }

    }
}
