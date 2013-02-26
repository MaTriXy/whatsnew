package info.evelio.whatsnew.task;

import com.codeslap.groundy.GroundyTask;
import com.codeslap.persistence.SqlAdapter;
import info.evelio.whatsnew.helper.PersistenceHelper;
import info.evelio.whatsnew.model.ApplicationEntry;
import info.evelio.whatsnew.model.EntrySnapshot;
import info.evelio.whatsnew.util.L;

import static info.evelio.whatsnew.model.ApplicationEntry.Contract.COLUMN_PACKAGE_NAME;

/**
 * @author Evelio Tarazona Cáceres <evelio@evelio.info>
 */
public abstract class PackageTask extends GroundyTask {
  public static final String PARAM_PACKAGE_NAME = "info.evelio.whatsnew.param.PACKAGE_NAME";
  static final String WHERE_PACKAGE_NAME_EQUALS = COLUMN_PACKAGE_NAME + " = ?";


  private SqlAdapter mAdapter;

  protected String getPackageName() {
    return getStringParam(PARAM_PACKAGE_NAME);
  }

  protected SqlAdapter getSqlAdapter() {
    if (mAdapter == null) {
      mAdapter = PersistenceHelper.getAdapter(getContext());
    }
    return mAdapter;
  }

  protected ApplicationEntry readApplicationEntry() {
    return getSqlAdapter()
        .findFirst(ApplicationEntry.class, WHERE_PACKAGE_NAME_EQUALS, new String[]{ getPackageName() });
  }

  protected void makeSnapshot(ApplicationEntry entry) {
    try {
      final EntrySnapshot snapshot = new EntrySnapshot.Builder().from(entry).build();
      getSqlAdapter().store(snapshot);
    } catch (Exception e) {
      L.e("wn:PTask", "Unable to create snapshot for " + e, e);
    }
  }

}
