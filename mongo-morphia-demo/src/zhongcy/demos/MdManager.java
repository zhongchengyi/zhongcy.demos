package zhongcy.demos;

import com.mongodb.MongoClient;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.converters.Converters;
import dev.morphia.converters.TypeConverter;
import dev.morphia.query.internal.MorphiaCursor;
import zhongcy.demos.converter.EnumOrginalConverter;
import zhongcy.demos.converter.IgnoreArrayAutoFieldsConverter;
import zhongcy.demos.model.MorphiaDemoModel;
import zhongcy.demos.model.SchoolClassLevel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class MdManager {

    public static final MdManager instance = new MdManager();

    MongoClient client;

    Morphia morphia;

    private HashMap<String, Datastore> dbDatastoreMap;

    private MdManager() {
        dbDatastoreMap = new HashMap<>();
    }

    public void t(MorphiaDemoModel data) {
        Datastore datastore = getHyDatastroe();
        datastore.save(data);

        MorphiaCursor<MorphiaDemoModel> cursor =
                datastore.createQuery(MorphiaDemoModel.class)
                        .find();
        List<MorphiaDemoModel> toList = cursor.toList();
        int size = toList.size();
    }

    private MongoClient getMongoClient() {
        if (client == null) {
            client = new MongoClient();
        }
        return client;
    }

    public Morphia getMorphia() {
        if (morphia == null) {
            morphia = new Morphia();
            try {
                Converters converters = morphia.getMapper().getConverters();
                Method getEncoder = Converters.class.getDeclaredMethod("getEncoder", Class.class);
                getEncoder.setAccessible(true);
                TypeConverter enco = ((TypeConverter) getEncoder.invoke(converters, SchoolClassLevel.class));
                converters.removeConverter(enco);
                converters.addConverter(new EnumOrginalConverter());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            morphia.mapPackage(MorphiaDemoModel.class.getPackage().getName());
        }
        return morphia;
    }

    /**
     * 获取数据的库
     *
     * @return
     */
    public Datastore getHyDatastroe() {
        return getDataStore("zhongcy");
    }

    public Datastore getDataStore(String dbName) {
        if (!dbDatastoreMap.containsKey(dbName)) {
            dbDatastoreMap.put(dbName, getMorphia().createDatastore(getMongoClient(), dbName));
        }
        return dbDatastoreMap.get(dbName);
    }
}
