package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import sifarnici.KategorijaInstruktorManagedBean;
import model.Kategorija;


// Napravi istu ovakvu klasu, nazovi je drukcije i ukloni komentare, 
// tj. aktiviraj, for petlju u getAsObject metodu - uradjeno u klasi "KategorijaInstruktorConverter2.java"
@FacesConverter("kategorijaInstruktorConverter")
public class KategorijaInstruktorConverter implements Converter{
	
	public KategorijaInstruktorConverter(){}
	
	@Override
	public Object getAsObject(FacesContext context,UIComponent arg1,String arg2){
		KategorijaInstruktorManagedBean katInstMB = context.getApplication().
				evaluateExpressionGet(context, "#{kategorijaInstruktorManagedBean}", KategorijaInstruktorManagedBean.class);
		throw new ConverterException(new FacesMessage(String.format("Can not convert "+arg2+" to kategorija")));
	}//getAsObject
	
	@Override
	public String getAsString(FacesContext arg0,UIComponent arg1,Object arg2){
		if(arg2==null){
			return null;
		}else{
			Kategorija kat = (Kategorija)arg2;
			return String.valueOf(kat.getIdKategorija());
		}
	}//getAsString
}
