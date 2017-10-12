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
            document.getElementById("divTablaNombres").scrollTop = elemento.scrollTop ;
            document.getElementById("divTotales").scrollTop = elemento.scrollTop ;
          }else{
            document.getElementById("divTablaNombres").scrollTop = elemento.scrollTop ;
            document.getElementById("divTablaDatos").scrollTop = elemento.scrollTop ;
          }
     }
}
