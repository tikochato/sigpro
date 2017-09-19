'use strict';
angular.module('indeterminate',[]);
/**
 * Directive for an indeterminate (tri-state) checkbox.
 * Based on the examples at http://stackoverflow.com/questions/12648466/how-can-i-get-angular-js-checkboxes-with-select-unselect-all-functionality-and-i
 */
angular.module('indeterminate')
  .directive('indeterminate', function() {
    return {
      require: '?ngModel',
      link: function(scope, el, attrs, ctrl) {
        ctrl.$formatters = [];
        ctrl.$parsers = [];
        ctrl.$render = function() {
          var d = ctrl.$viewValue;
          el.data('checked', d);
          switch(d){
          case true:
            el.prop('indeterminate', false);
            el.prop('checked', true);
            break;
          case false:
            el.prop('indeterminate', false);
            el.prop('checked', false);
            break;
          default:
            el.prop('indeterminate', true);
          }
        };
        el.bind('click', function() {
          var d;
          switch(el.data('checked')){
          case false:
            d = true;
            break;
          case true:
            d = null;
            break;
          default:
            d = false;
          }
          ctrl.$setViewValue(d);
          scope.$apply(ctrl.$render);
        });
      }
    };
  })