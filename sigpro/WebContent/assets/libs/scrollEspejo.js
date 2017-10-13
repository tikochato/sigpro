var divActivo;
var scrollPosicion;
	
function activarScroll(id){
	divActivo = id;
	scrollPosicion = document.getElementById("divTablaDatos").scrollLeft;
}

function scrollEspejo(elemento) {
	 if (elemento.id == divActivo){
          if(elemento.id == 'divTablaNombres'){
            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
            document.getElementById("divTotales").scrollTop = elemento.scrollTop ;
          }else if(elemento.id == 'divTablaDatos'){
        	if(Math.abs(scrollPosicion-elemento.scrollLeft)<10){//bloquear scroll horizontal
        		elemento.scrollLeft = scrollPosicion;
        	}
        	if(document.getElementById("divCabecerasDatos")){
        		document.getElementById("divCabecerasDatos").scrollLeft = elemento.scrollLeft;
        	}
        	if(document.getElementById("divTablaNombres")){
        		document.getElementById("divTablaNombres").scrollTop = elemento.scrollTop ;
        	}
        	if(document.getElementById("divTotales")){
        		document.getElementById("divTotales").scrollTop = elemento.scrollTop ;
        	}
          }else if(elemento.id == 'divTablaDatosTot'){
        	  if(Math.abs(scrollPosicion-elemento.scrollLeft)<10){//bloquear scroll horizontal
          		elemento.scrollLeft = scrollPosicion;
          	}
          }else{
            document.getElementById("divTablaNombres").scrollTop = elemento.scrollTop ;
            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
          }
     }
}
