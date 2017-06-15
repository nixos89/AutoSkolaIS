package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import sifarnici.KategorijaInstruktorManagedBean;
import managedBeans.UnosInstruktoraManagedBean;
import model.Kategorija;


// PARAZIT klasa - BAD SMELL!!!
@FacesConverter("kategorijaZa1InstruktorConverter")
public class KategorijaZa1InstruktorConverter implements Converter{
	
	public KategorijaZa1InstruktorConverter(){}
	
	@Override
	public Object getAsObject(FacesContext context,UIComponent arg1,String arg2){
		UnosInstruktoraManagedBean katInstMB = context.getApplication().
				evaluateExpressionGet(context, "#{unosInstruktoraManagedBean}", UnosInstruktoraManagedBean.class);
		for(Kategorija kat : katInstMB.vratiKategorijeInstruktora()){
			if(String.valueOf(kat.getIdKategorija()).equals(arg2)){
				return kat;
			}
		}
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
