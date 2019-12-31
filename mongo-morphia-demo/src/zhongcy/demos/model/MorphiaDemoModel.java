package zhongcy.demos.model;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

@Entity(value = "MorphiaDemoModel", noClassnameStored = true)
public class MorphiaDemoModel {

    @Id
   public   ObjectId id;

    public SchoolClassLevel schoolClassLevel;

    public SchoolClassLevel1 schoolClassLevel1;
}
