package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import sifarnici.InstruktorKategorijaManagedBean;
import sifarnici.KategorijaInstruktorManagedBean;
import model.Instruktor;
import model.Kategorija;


// Napravi istu ovakvu klasu, nazovi je drukcije i ukloni komentare, 
// tj. aktiviraj, for petlju u getAsObject metodu
@FacesConverter("kategorijaInstruktorConverter2")
public class KategorijaInstruktorConverter2 implements Converter{
	
	public KategorijaInstruktorConverter2(){}
	
	@Override
	public Object getAsObject(FacesContext context,UIComponent arg1,String arg2){
		InstruktorKategorijaManagedBean instKatMB = context.getApplication().
				evaluateExpressionGet(context, "#{instruktorKategorijaManagedBean}", InstruktorKategorijaManagedBean.class);
		for(Instruktor inst : instKatMB.getSviInstruktori()){
			if(String.valueOf(inst.getIdInstruktor()).equals(arg2)){
				return inst;
			}
		}
		throw new ConverterException(new FacesMessage(String.format("Can not convert "+arg2+" to kategorija")));
	}//getAsObject
	
	@Override
	public String getAsString(FacesContext arg0,UIComponent arg1,Object arg2){
		if(arg2==null){
			return null;
		}else{
			Instruktor inst = (Instruktor)arg2;
			return String.valueOf(inst.getIdInstruktor());
		}
	}//getAsString
}