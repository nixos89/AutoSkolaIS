package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import model.Instruktor;
import sifarnici.KandidatInstruktorManagedBean;

@FacesConverter("kandidatInstruktorConverter")
public class KandidatInstruktorConverter implements Converter{
	
	public KandidatInstruktorConverter(){}
	
	@Override
	public Object getAsObject(FacesContext context,UIComponent arg1,String arg2){
		KandidatInstruktorManagedBean kandInstMB = context.getApplication().
				evaluateExpressionGet(context, "#{kandidatInstruktorManagedBean}", KandidatInstruktorManagedBean.class);
		for(Instruktor inst : kandInstMB.getSviInstruktori()){
			if(String.valueOf(inst.getIdInstruktor()).equals(arg2)){
				return inst;
			}
		}
		throw new ConverterException(new FacesMessage(String.format("Can not convert "+arg2+" to instruktor")));
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
