var app = angular.module('documentoAdjunto',[]);

app.factory('documentoAdjunto',['$mdDialog','$uibModal', '$http',
	function($mdDialog,$uibModal,$http){
	
	return{
		getModalDocumento: function($scope, objetoId, tipoObjetoId){
			return $uibModal.open({
			    animation : 'true',
			    ariaLabelledBy : 'modal-title',
			    ariaDescribedBy : 'modal-body',
			    templateUrl : '/app/components/documentosadjuntos/documentosadjuntos.jsp',
			    controller: 'ModalDialogController',
			    controllerAs: 'doctos',
			    backdrop : 'static',
			    size : 'lg',
			    scope: $scope,
			    resolve : {
			    	tipoObjeto : function() {
						return tipoObjetoId ;
					},
					elementoId: function(){
						return objetoId;
					},
			    }

			});
		},
		
		getDocumentosAdjuntos: function(objetoId, tipoObjetoId){
			var formatData = new FormData();
			formatData.append("accion","getDocumentos");
			formatData.append("idObjeto", objetoId);
			formatData.append("idTipoObjeto", tipoObjetoId);
			return $http.post('/SDocumentosAdjuntos', formatData, {
				headers: {'Content-Type': undefined},
				transformRequest: angular.identity,
			}).then(function(response) {
				if (response.data.success) {
					return response.data.documentos;
				}
			});
		}
	}
}])

app.controller('ModalDialogController', [ '$uibModalInstance',
	'$scope', '$http', '$interval', 'i18nService', 'Utilidades',
	'$timeout', '$log', 'tipoObjeto', 'elementoId',ModalDialogController ]);

function ModalDialogController($uibModalInstance, $scope, $http, $interval, i18nService, $utilidades, $timeout, $log, tipoObjeto, elementoId) {
	var mi = this;
	
	if (tipoObjeto > 0 && elementoId > 0){
		var formatData = new FormData();
		formatData.append("accion","getDocumentos");
		formatData.append("idObjeto", elementoId);
		formatData.append("idTipoObjeto", tipoObjeto);
		formatData.append("t",moment().unix());
		$http.post('/SDocumentosAdjuntos', formatData, {
			headers: {'Content-Type': undefined},
			transformRequest: angular.identity,
		}).then(function(response) {
			if (response.data.success) {
				mi.tdocumentos=[];
				var documentos = response.data.documentos;
				for (var i = 0; i < documentos.length; i++){
					mi.tdocumentos.push(
						{
							'id' : documentos[i].id,
							'extension' : documentos[i].extension,
							'nombre' : documentos[i].nombre,
						}
					)
				}	
			}else{

			}
		});
	}
	
	mi.agregarDocumento=function(){
		if ($scope.documentos!=null){
			var formatData = new FormData();
			formatData.append("file",$scope.documentos);
			formatData.append("accion",'agregarDocumento');
			formatData.append("idObjeto", elementoId);
			formatData.append("idTipoObjeto",tipoObjeto );
			formatData.append("esNuevo",true);
			$http.post('/SDocumentosAdjuntos', formatData, {
				headers: {'Content-Type': undefined},
				transformRequest: angular.identity,
			}).then(
					function(response) {
						if (response.data.success) {
							$utilidades.mensaje('success','Agregado exitosamente');
							$uibModalInstance.close(response.data.documentos);
						}else{
							if(response.data.existe_archivo)
								$utilidades.mensaje('danger','El archivo que desea subir ya existe');
							else{
								$utilidades.mensaje('danger','Error al guardar el documento adjunto');
							}
							$uibModalInstance.close("");
						}
			});
		}else{
			$utilidades.mensaje('danger','Debe seleccionar un archivo');
		}
	};
	
	String.prototype.replaceAll = function(search, replacement) {
	    var target = this;
	    return target.replace(new RegExp(search, 'g'), replacement);
	};
	
	$scope.cargarDocumento=function(event){
		$scope.documentos = event.target.files[0];      
		$scope.nombreDocumento = $scope.documentos.name | null;
	}	
	
	mi.ok = function() {
		$uibModalInstance.close(true);
	};
	
	mi.descargarDocumento= function(documento){
		var url = "/SDocumentosAdjuntos?accion=getDescarga&id="+documento.id;
		window.location.href = url;
	}
	
	mi.eliminarDocumento= function(documento, selectedValue){
		$http.post('/SDocumentosAdjuntos?accion=eliminarDocumento&id='+documento.id)
		.then(function successCAllback(response){
			if (response.data.success){
				var indice = mi.tdocumentos.indexOf(documento);
				if (indice !== -1) {
			       mi.tdocumentos.splice(indice, 1);		       
			    }
				mi.tdocumentos = [];
				mi.cargarDocumentos(selectedValue);
			}
		});
	};
}