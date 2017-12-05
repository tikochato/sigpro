<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/assets/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/angular-chart.min.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/ui-grid.min.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/font-awesome.min.css">
<link rel="stylesheet" type="text/css" href="/assets/css/timeline/angular-timeline-animations.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/timeline/angular-timeline-bootstrap.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/timeline/angular-timeline.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/main.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/bootstrap.adds.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/kanban.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/ivh-treeview.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/angular-tree/tree-control-attribute.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/angular-tree/tree-control.css" />
<link rel="stylesheet" type="text/css" href="/assets/css/angular-resizable.min.css" />

<script type="text/javascript" src="/assets/libs/angular/angular.js"></script>
<script type="text/javascript" src="/assets/libs/angular/angular-route.min.js"></script>
<script type="text/javascript" src="/assets/libs/angular/angular-animate.min.js"></script>
<script type="text/javascript" src="/assets/libs/angular/angular-touch.min.js"></script>
<script type="text/javascript" src="/assets/libs/angular/angular-messages.min.js"></script>
<script type="text/javascript" src="/assets/libs/angular/angular-aria.min.js"></script>
<script type="text/javascript" src="/assets/libs/ui-bootstrap-tpls-2.5.0.min.js"></script> 
<script type="text/javascript" src="/assets/libs/moment.min.js"></script>
<script type="text/javascript" src="/assets/libs/Chart.min.js"></script>
<script type="text/javascript" src="/assets/libs/angular-chart.min.js"></script>
<script type="text/javascript" src="/assets/libs/ui-grid.min.js"></script>  
<script type="text/javascript" src="/assets/libs/maps/angular-simple-logger.min.js"></script> 
<script type="text/javascript" src="/assets/libs/maps/lodash.js"></script> 
<script type="text/javascript" src="/assets/libs/maps/angular-google-maps.min.js"></script> 
<script type="text/javascript" src="/assets/libs/loadOnDemand.js"></script>
<script type="text/javascript" src="/assets/libs/numeral.min.js"></script>
<script type="text/javascript" src="/assets/libs/numeral.es.js"></script> 
<script type="text/javascript" src="/assets/libs/ng-device-detector.min.js"></script>
<script type="text/javascript" src="/assets/libs/re-tree.min.js"></script>
<script type="text/javascript" src="/assets/libs/angular-timeline.js"></script>
<script type="text/javascript" src="/assets/libs/angular-scroll-animate.js"></script>
<script type="text/javascript" src="/assets/libs/angular/angular-sanitize.min.js"></script>
<script type="text/javascript" src="/assets/libs/justgage.js"></script>  
<script type="text/javascript" src="/assets/libs/raphael-2.1.4.min.js"></script>
<script type="text/javascript" src="/assets/libs/contextMenu.js"></script>
<script type="text/javascript" src="/assets/libs/smart-table.min.js"></script>
<script type="text/javascript" src="/assets/libs/ng-file-upload-shim.min.js"></script> 
<script type="text/javascript" src="/assets/libs/ng-file-upload.min.js"></script> 
<script type="text/javascript" src="/assets/libs/angular-flash.min.js"></script>
<script type="text/javascript" src="/assets/libs/angular-material.min.js"></script>
<script type="text/javascript" src="/assets/libs/utilidades.js"></script>
<script type="text/javascript" src="/assets/libs/documentoAdjunto.js"></script>
<script type="text/javascript" src="/assets/libs/dialogoConfirmacion.js"></script>
<script type="text/javascript" src="/assets/libs/ivh-treeview.js"></script>
<script type="text/javascript" src="/assets/libs/ganttchart/DlhSoft.Data.HTML.Controls.js"></script>
<script type="text/javascript" src="/assets/libs/ganttchart/DlhSoft.ProjectData.GanttChart.HTML.Controls.js"></script>
<script type="text/javascript" src="/assets/libs/ganttchart/DlhSoft.ProjectData.GanttChart.HTML.Controls.Extras.js"></script>
<script type="text/javascript" src="/assets/libs/ganttchart/DlhSoft.ProjectData.GanttChart.Angular.Directives.js"></script>
<script type="text/javascript" src="/assets/libs/kanbanboard/DlhSoft.Kanban.Angular.Components.js"></script>
<script type="text/javascript" src="/assets/libs/angular-input-masks-standalone.js"></script>
<link rel="stylesheet" type="text/css" href="/assets/css/angular-bootstrap-toggle.min.css" />
<script type="text/javascript" src="/assets/libs/angular/angular-bootstrap-toggle.js"></script>
<script type="text/javascript" src="/assets/libs/Chart.PieceLabel.js"></script>
<script type="text/javascript" src="/assets/libs/angularjs-dropdown-multiselect.js"></script>
<script type="text/javascript" src="/assets/libs/angucomplete-alt.js"></script>
<script type="text/javascript" src="/assets/libs/angular-tree-control.js"></script>
<script type="text/javascript" src="/assets/libs/tri-state.js"></script>
<script type="text/javascript" src="/assets/libs/angular-vs-repeat.min.js"></script>
<script type="text/javascript" src="/assets/libs/angular-resizable.min.js"></script>

<script type="text/javascript" src="/assets/libs/scrollEspejo.js"></script>
<script type="text/javascript" src="/app/components/meta/meta.controller.js"></script>
<script type="text/javascript" src="/app/components/desembolso/desembolso.controller.js"></script>
<script type="text/javascript" src="/app/components/riesgo/riesgo.controller.js"></script>
<script type="text/javascript" src="/app/components/adquisicion/adquisicion.controller.js"></script>
<script type="text/javascript" src="/assets/libs/historia.js"></script>

<!-- <script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBPq-t4dJ1GV1kdtXoVZfG7PtfEAHrhr00&callback=initMap" type="text/javascript"></script> -->
<script>
  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-74443600-1', 'auto');
  ga('send', 'pageview');
</script>

   
   