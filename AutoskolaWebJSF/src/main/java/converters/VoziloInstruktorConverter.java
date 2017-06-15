package converters;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import sifarnici.VoziloInstruktorManagedBean;
import model.Vozilo;

@FacesConverter("voziloInstruktorConverter")
public class VoziloInstruktorConverter implements Converter{
	
	public VoziloInstruktorConverter(){}
	
	@Override
	public Object getAsObject(FacesContext context,UIComponent arg1,String arg2){
		VoziloInstruktorManagedBean vozInstMB = context.getApplication().
				evaluateExpressionGet(context, "#{voziloInstruktorManagedBean}", VoziloInstruktorManagedBean.class);
		for(Vozilo v:vozInstMB.getSvaVozila()){
			if(String.valueOf(v.getIdVozila()).equals(arg2)){
				return v;
			}
		}
		throw new ConverterException(new FacesMessage(String.format("Can not convert "+arg2+" to vozilo")));
	}//getAsObject
	
	@Override
	public String getAsString(FacesContext arg0,UIComponent arg1,Object arg2){
		if(arg2==null){
			return null;
		}else{
			Vozilo v = (Vozilo)arg2;
			return String.valueOf(v.getIdVozila());
		}
	}
}
