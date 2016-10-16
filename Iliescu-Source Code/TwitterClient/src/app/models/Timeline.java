package app.models;

import com.apress.framework.objecttypes.Model;

public interface Timeline extends Model
{
    public Tweet goForward();

    public Tweet goBack();
}
