(()=>{var e={42:function(e){e.exports=function(e){function t(o){if(n[o])return n[o].exports;var i=n[o]={exports:{},id:o,loaded:!1};return e[o].call(i.exports,i,i.exports,t),i.loaded=!0,i.exports}var n={};return t.m=e,t.c=n,t.p="dist/",t(0)}([function(e,t,n){"use strict";function o(e){return e&&e.__esModule?e:{default:e}}var i=Object.assign||function(e){for(var t=1;t<arguments.length;t++){var n=arguments[t];for(var o in n)Object.prototype.hasOwnProperty.call(n,o)&&(e[o]=n[o])}return e},r=(o(n(1)),n(6)),a=o(r),c=o(n(7)),s=o(n(8)),l=o(n(9)),d=o(n(10)),u=o(n(11)),m=o(n(14)),f=[],p=!1,b={offset:120,delay:0,easing:"ease",duration:400,disable:!1,once:!1,startEvent:"DOMContentLoaded",throttleDelay:99,debounceDelay:50,disableMutationObserver:!1},g=function(){if(arguments.length>0&&void 0!==arguments[0]&&arguments[0]&&(p=!0),p)return f=(0,u.default)(f,b),(0,d.default)(f,b.once),f},v=function(){f=(0,m.default)(),g()};e.exports={init:function(e){b=i(b,e),f=(0,m.default)();var t=document.all&&!window.atob;return function(e){return!0===e||"mobile"===e&&l.default.mobile()||"phone"===e&&l.default.phone()||"tablet"===e&&l.default.tablet()||"function"==typeof e&&!0===e()}(b.disable)||t?void f.forEach((function(e,t){e.node.removeAttribute("data-aos"),e.node.removeAttribute("data-aos-easing"),e.node.removeAttribute("data-aos-duration"),e.node.removeAttribute("data-aos-delay")})):(b.disableMutationObserver||s.default.isSupported()||(console.info('\n      aos: MutationObserver is not supported on this browser,\n      code mutations observing has been disabled.\n      You may have to call "refreshHard()" by yourself.\n    '),b.disableMutationObserver=!0),document.querySelector("body").setAttribute("data-aos-easing",b.easing),document.querySelector("body").setAttribute("data-aos-duration",b.duration),document.querySelector("body").setAttribute("data-aos-delay",b.delay),"DOMContentLoaded"===b.startEvent&&["complete","interactive"].indexOf(document.readyState)>-1?g(!0):"load"===b.startEvent?window.addEventListener(b.startEvent,(function(){g(!0)})):document.addEventListener(b.startEvent,(function(){g(!0)})),window.addEventListener("resize",(0,c.default)(g,b.debounceDelay,!0)),window.addEventListener("orientationchange",(0,c.default)(g,b.debounceDelay,!0)),window.addEventListener("scroll",(0,a.default)((function(){(0,d.default)(f,b.once)}),b.throttleDelay)),b.disableMutationObserver||s.default.ready("[data-aos]",v),f)},refresh:g,refreshHard:v}},function(e,t){},,,,,function(e,t){(function(t){"use strict";function n(e,t,n){function i(t){var n=u,o=m;return u=m=void 0,v=t,p=e.apply(o,n)}function a(e){var n=e-g;return void 0===g||n>=t||n<0||L&&e-v>=f}function s(){var e=E();return a(e)?l(e):void(b=setTimeout(s,function(e){var n=t-(e-g);return L?w(n,f-(e-v)):n}(e)))}function l(e){return b=void 0,k&&u?i(e):(u=m=void 0,p)}function d(){var e=E(),n=a(e);if(u=arguments,m=this,g=e,n){if(void 0===b)return function(e){return v=e,b=setTimeout(s,t),y?i(e):p}(g);if(L)return b=setTimeout(s,t),i(g)}return void 0===b&&(b=setTimeout(s,t)),p}var u,m,f,p,b,g,v=0,y=!1,L=!1,k=!0;if("function"!=typeof e)throw new TypeError(c);return t=r(t)||0,o(n)&&(y=!!n.leading,f=(L="maxWait"in n)?h(r(n.maxWait)||0,t):f,k="trailing"in n?!!n.trailing:k),d.cancel=function(){void 0!==b&&clearTimeout(b),v=0,u=g=m=b=void 0},d.flush=function(){return void 0===b?p:l(E())},d}function o(e){var t=void 0===e?"undefined":a(e);return!!e&&("object"==t||"function"==t)}function i(e){return"symbol"==(void 0===e?"undefined":a(e))||function(e){return!!e&&"object"==(void 0===e?"undefined":a(e))}(e)&&y.call(e)==l}function r(e){if("number"==typeof e)return e;if(i(e))return s;if(o(e)){var t="function"==typeof e.valueOf?e.valueOf():e;e=o(t)?t+"":t}if("string"!=typeof e)return 0===e?e:+e;e=e.replace(d,"");var n=m.test(e);return n||f.test(e)?p(e.slice(2),n?2:8):u.test(e)?s:+e}var a="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},c="Expected a function",s=NaN,l="[object Symbol]",d=/^\s+|\s+$/g,u=/^[-+]0x[0-9a-f]+$/i,m=/^0b[01]+$/i,f=/^0o[0-7]+$/i,p=parseInt,b="object"==(void 0===t?"undefined":a(t))&&t&&t.Object===Object&&t,g="object"==("undefined"==typeof self?"undefined":a(self))&&self&&self.Object===Object&&self,v=b||g||Function("return this")(),y=Object.prototype.toString,h=Math.max,w=Math.min,E=function(){return v.Date.now()};e.exports=function(e,t,i){var r=!0,a=!0;if("function"!=typeof e)throw new TypeError(c);return o(i)&&(r="leading"in i?!!i.leading:r,a="trailing"in i?!!i.trailing:a),n(e,t,{leading:r,maxWait:t,trailing:a})}}).call(t,function(){return this}())},function(e,t){(function(t){"use strict";function n(e){var t=void 0===e?"undefined":r(e);return!!e&&("object"==t||"function"==t)}function o(e){return"symbol"==(void 0===e?"undefined":r(e))||function(e){return!!e&&"object"==(void 0===e?"undefined":r(e))}(e)&&v.call(e)==s}function i(e){if("number"==typeof e)return e;if(o(e))return c;if(n(e)){var t="function"==typeof e.valueOf?e.valueOf():e;e=n(t)?t+"":t}if("string"!=typeof e)return 0===e?e:+e;e=e.replace(l,"");var i=u.test(e);return i||m.test(e)?f(e.slice(2),i?2:8):d.test(e)?c:+e}var r="function"==typeof Symbol&&"symbol"==typeof Symbol.iterator?function(e){return typeof e}:function(e){return e&&"function"==typeof Symbol&&e.constructor===Symbol&&e!==Symbol.prototype?"symbol":typeof e},a="Expected a function",c=NaN,s="[object Symbol]",l=/^\s+|\s+$/g,d=/^[-+]0x[0-9a-f]+$/i,u=/^0b[01]+$/i,m=/^0o[0-7]+$/i,f=parseInt,p="object"==(void 0===t?"undefined":r(t))&&t&&t.Object===Object&&t,b="object"==("undefined"==typeof self?"undefined":r(self))&&self&&self.Object===Object&&self,g=p||b||Function("return this")(),v=Object.prototype.toString,y=Math.max,h=Math.min,w=function(){return g.Date.now()};e.exports=function(e,t,o){function r(t){var n=u,o=m;return u=m=void 0,v=t,p=e.apply(o,n)}function c(e){var n=e-g;return void 0===g||n>=t||n<0||L&&e-v>=f}function s(){var e=w();return c(e)?l(e):void(b=setTimeout(s,function(e){var n=t-(e-g);return L?h(n,f-(e-v)):n}(e)))}function l(e){return b=void 0,k&&u?r(e):(u=m=void 0,p)}function d(){var e=w(),n=c(e);if(u=arguments,m=this,g=e,n){if(void 0===b)return function(e){return v=e,b=setTimeout(s,t),E?r(e):p}(g);if(L)return b=setTimeout(s,t),r(g)}return void 0===b&&(b=setTimeout(s,t)),p}var u,m,f,p,b,g,v=0,E=!1,L=!1,k=!0;if("function"!=typeof e)throw new TypeError(a);return t=i(t)||0,n(o)&&(E=!!o.leading,f=(L="maxWait"in o)?y(i(o.maxWait)||0,t):f,k="trailing"in o?!!o.trailing:k),d.cancel=function(){void 0!==b&&clearTimeout(b),v=0,u=g=m=b=void 0},d.flush=function(){return void 0===b?p:l(w())},d}}).call(t,function(){return this}())},function(e,t){"use strict";function n(e){var t=void 0,o=void 0;for(t=0;t<e.length;t+=1){if((o=e[t]).dataset&&o.dataset.aos)return!0;if(o.children&&n(o.children))return!0}return!1}function o(){return window.MutationObserver||window.WebKitMutationObserver||window.MozMutationObserver}function i(e){e&&e.forEach((function(e){var t=Array.prototype.slice.call(e.addedNodes),o=Array.prototype.slice.call(e.removedNodes);if(n(t.concat(o)))return r()}))}Object.defineProperty(t,"__esModule",{value:!0});var r=function(){};t.default={isSupported:function(){return!!o()},ready:function(e,t){var n=window.document,a=new(o())(i);r=t,a.observe(n.documentElement,{childList:!0,subtree:!0,removedNodes:!0})}}},function(e,t){"use strict";function n(){return navigator.userAgent||navigator.vendor||window.opera||""}Object.defineProperty(t,"__esModule",{value:!0});var o=function(){function e(e,t){for(var n=0;n<t.length;n++){var o=t[n];o.enumerable=o.enumerable||!1,o.configurable=!0,"value"in o&&(o.writable=!0),Object.defineProperty(e,o.key,o)}}return function(t,n,o){return n&&e(t.prototype,n),o&&e(t,o),t}}(),i=/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i,r=/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i,a=/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino|android|ipad|playbook|silk/i,c=/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i,s=function(){function e(){!function(e,t){if(!(e instanceof t))throw new TypeError("Cannot call a class as a function")}(this,e)}return o(e,[{key:"phone",value:function(){var e=n();return!(!i.test(e)&&!r.test(e.substr(0,4)))}},{key:"mobile",value:function(){var e=n();return!(!a.test(e)&&!c.test(e.substr(0,4)))}},{key:"tablet",value:function(){return this.mobile()&&!this.phone()}}]),e}();t.default=new s},function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});t.default=function(e,t){var n=window.pageYOffset,o=window.innerHeight;e.forEach((function(e,i){!function(e,t,n){var o=e.node.getAttribute("data-aos-once");t>e.position?e.node.classList.add("aos-animate"):void 0!==o&&("false"===o||!n&&"true"!==o)&&e.node.classList.remove("aos-animate")}(e,o+n,t)}))}},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=function(e){return e&&e.__esModule?e:{default:e}}(n(12));t.default=function(e,t){return e.forEach((function(e,n){e.node.classList.add("aos-init"),e.position=(0,o.default)(e.node,t.offset)})),e}},function(e,t,n){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=function(e){return e&&e.__esModule?e:{default:e}}(n(13));t.default=function(e,t){var n=0,i=0,r=window.innerHeight,a={offset:e.getAttribute("data-aos-offset"),anchor:e.getAttribute("data-aos-anchor"),anchorPlacement:e.getAttribute("data-aos-anchor-placement")};switch(a.offset&&!isNaN(a.offset)&&(i=parseInt(a.offset)),a.anchor&&document.querySelectorAll(a.anchor)&&(e=document.querySelectorAll(a.anchor)[0]),n=(0,o.default)(e).top,a.anchorPlacement){case"top-bottom":break;case"center-bottom":n+=e.offsetHeight/2;break;case"bottom-bottom":n+=e.offsetHeight;break;case"top-center":n+=r/2;break;case"bottom-center":n+=r/2+e.offsetHeight;break;case"center-center":n+=r/2+e.offsetHeight/2;break;case"top-top":n+=r;break;case"bottom-top":n+=e.offsetHeight+r;break;case"center-top":n+=e.offsetHeight/2+r}return a.anchorPlacement||a.offset||isNaN(t)||(i=t),n+i}},function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});t.default=function(e){for(var t=0,n=0;e&&!isNaN(e.offsetLeft)&&!isNaN(e.offsetTop);)t+=e.offsetLeft-("BODY"!=e.tagName?e.scrollLeft:0),n+=e.offsetTop-("BODY"!=e.tagName?e.scrollTop:0),e=e.offsetParent;return{top:n,left:t}}},function(e,t){"use strict";Object.defineProperty(t,"__esModule",{value:!0});t.default=function(e){return e=e||document.querySelectorAll("[data-aos]"),Array.prototype.map.call(e,(function(e){return{node:e}}))}}])},242:()=>{const e=document.getElementById("deactivate-btn"),t=document.getElementById("deactivate-cancel-btn"),n=document.getElementById("deactivate-modal"),o=document.getElementById("deactivate-backdrop"),i=document.getElementById("deactivate-panel");if(e&&t&&n&&o&&i){function r(){n.classList.toggle("pointer-events-none"),o.classList.toggle("opacity-0"),o.classList.toggle("opacity-100"),i.classList.toggle("opacity-0"),i.classList.toggle("translate-y-4"),i.classList.toggle("translate-y-0"),i.classList.toggle("sm:scale-95"),i.classList.toggle("sm:scale-100")}e.addEventListener("click",r),t.addEventListener("click",r)}},597:()=>{document.addEventListener("DOMContentLoaded",(()=>{const e=document.querySelector(".marquee-container"),t=e?.querySelectorAll("img")||null;if(e&&t){const n=()=>{const n=Array.from(t).slice(0,t.length/2).reduce(((e,t)=>e+t.offsetWidth),0)/e.offsetWidth*100;e.style.setProperty("--marquee-offset",`-${n}%`)};n();let o=window.devicePixelRatio;const i=()=>{const t=window.devicePixelRatio;t!==o&&(e.classList.remove("animate-marquee"),e.offsetWidth,e.classList.add("animate-marquee"),n(),o=t)};window.addEventListener("resize",i)}}))},924:()=>{const e=document.querySelectorAll(".details-btn");e&&e.forEach((e=>{e.addEventListener("click",(()=>{const t=e.closest(".order").querySelector(".order-details");t.classList.contains("order-details")?(t.classList.toggle("hidden"),t.classList.contains("hidden")?e.textContent="View Details":e.textContent="Close Details"):console.warn("Incorrect element")}))}));const t=document.querySelectorAll(".status-btn");t&&t.forEach((e=>{e.addEventListener("click",(()=>{const t=e.closest(".order"),n=t.querySelector(".status");if(e.textContent.includes("Shipped"))n.classList.replace("text-yellow-600","text-orange-600"),n.textContent="Shipped";else{n.classList.replace("text-orange-600","text-green-600"),n.textContent="Completed";const r=t.querySelector(".form-rating"),a=r.querySelector(".cancel-rating"),c=r.querySelector(".review-modal"),s=r.querySelector(".review-backdrop"),l=r.querySelector(".review-panel");function o(){c.classList.toggle("pointer-events-none"),s.classList.toggle("opacity-0"),s.classList.toggle("opacity-100"),l.classList.toggle("opacity-0"),l.classList.toggle("translate-y-4"),l.classList.toggle("translate-y-0"),l.classList.toggle("sm:scale-95"),l.classList.toggle("sm:scale-100")}function i(e){const t=document.querySelectorAll(`.${e} .star`);t.forEach((e=>{e.addEventListener("click",(()=>{const n=e.getAttribute("data-value");t.forEach(((e,t)=>{e.textContent=t<n?"⭐":"☆"})),e.parentElement.setAttribute("data-rating",n)}))}))}o(),a.addEventListener("click",o),i("seller-rating"),i("order-rating")}e.remove()}))}))},8:()=>{const e=document.querySelector(".register-user-input"),t=document.querySelector(".register-user-status");e&&t&&e.addEventListener("input",(()=>{const n=e.value;0!==n.trim().length?fetch(`/register/validate-username?username=${encodeURIComponent(n)}`).then((e=>e.json())).then((e=>{e?t.classList.remove("hidden"):t.classList.add("hidden")})).catch((e=>console.error("Error checking username:",e))):t.classList.add("hidden")}))},702:()=>{const e=document.getElementById("images"),t=document.getElementById("preview");let n=[];e&&e.addEventListener("change",(function(e){const o=Array.from(e.target.files);n.length+o.length>5||(o.forEach((e=>{["image/jpeg","image/png"].includes(e.type)&&(n.push(e),function(e){const o=new FileReader;o.onload=function(o){const i=document.createElement("img");i.src=o.target.result,i.alt="Uploaded Image",i.classList.add("w-30","h-30","object-cover","rounded");const r=document.createElement("button");r.textContent="Remove",r.classList.add("text-white","font-semibld","text-sm","mt-2","bg-red-500","rounded-full","p-2"),r.addEventListener("click",(()=>{const o=n.indexOf(e);o>-1&&n.splice(o,1),t.removeChild(a)}));const a=document.createElement("div");a.classList.add("flex","flex-col","items-center"),a.appendChild(i),a.appendChild(r),t.appendChild(a)},o.readAsDataURL(e)}(e))})),e.target.value="")}))},973:()=>{const e=document.getElementById("main-image"),t=document.querySelectorAll(".thumbnail"),n=document.getElementById("prev"),o=document.getElementById("next");if(e&&t&&n&&o){let s=0;function i(n){const o=document.querySelector(".thumbnail.active");o&&o.classList.remove("active","border-gray-700"),t[n].classList.add("active","border-gray-700"),e.src=t[n].src}t.forEach(((e,t)=>{e.addEventListener("click",(()=>{s=t,i(s)}))})),n.addEventListener("click",(()=>{s=s>0?s-1:t.length-1,i(s)})),o.addEventListener("click",(()=>{s=s<t.length-1?s+1:0,i(s)}))}const r=document.getElementById("heart"),a=document.getElementById("outlined-heart"),c=document.getElementById("filled-heart");r&&a&&c&&r.addEventListener("click",(()=>{a.classList.toggle("hidden"),c.classList.toggle("hidden")}))}},t={};function n(o){var i=t[o];if(void 0!==i)return i.exports;var r=t[o]={exports:{}};return e[o].call(r.exports,r,r.exports,n),r.exports}n.n=e=>{var t=e&&e.__esModule?()=>e.default:()=>e;return n.d(t,{a:t}),t},n.d=(e,t)=>{for(var o in t)n.o(t,o)&&!n.o(e,o)&&Object.defineProperty(e,o,{enumerable:!0,get:t[o]})},n.o=(e,t)=>Object.prototype.hasOwnProperty.call(e,t),(()=>{"use strict";var e=n(42),t=n.n(e);n(242);const o=document.getElementById("scrollToTop");function i(e,t,n,o){e.classList.replace("font-semibold","font-bold"),e.classList.add("border"),n.classList.remove("hidden"),t.classList.replace("font-bold","font-semibold"),t.classList.remove("border"),o.classList.add("hidden")}o&&(window.addEventListener("scroll",(()=>{window.scrollY>1e3?(o.classList.add("opacity-100"),o.classList.remove("opacity-0")):(o.classList.add("opacity-0"),o.classList.remove("opacity-100"))})),o.addEventListener("click",(()=>{window.scrollTo({top:0,behavior:"smooth"})})));const r=document.getElementById("purchasedBtn"),a=document.getElementById("shippedBtn"),c=document.getElementById("purchased-orders"),s=document.getElementById("shipped-orders");r&&a&&c&&s&&(r.addEventListener("click",(()=>i(r,a,c,s))),a.addEventListener("click",(()=>i(a,r,s,c))));const l=document.getElementById("items-btn"),d=document.getElementById("reviews-btn"),u=document.getElementById("items-section"),m=document.getElementById("reviews-section");l&&d&&u&&m&&(l.addEventListener("click",(()=>i(l,d,u,m))),d.addEventListener("click",(()=>i(d,l,m,u))));const f=document.querySelectorAll(".add-to-cart-btn");if(f){f.forEach((e=>{e.addEventListener("click",(()=>{e.classList.add("bg-pink-700","hover:bg-pink-700"),e.classList.remove("bg-zinc-900","hover:bg-zinc-700"),setTimeout((()=>{e.classList.remove("bg-pink-700","hover:bg-pink-800"),e.classList.add("bg-zinc-900","hover:bg-zinc-700")}),2e3)}))}));const e=document.querySelectorAll(".remove-btn");e&&e.forEach((e=>{e.addEventListener("click",(()=>{e.closest(".item").remove()}))}))}let p=null;function b(e,t){const n=document.getElementById(e),o=document.getElementById(t);n&&o?(n.addEventListener("click",(()=>{p&&p!==o&&g(p),g(o),p=o.classList.contains("opacity-100")?o:null})),o.addEventListener("mouseleave",(()=>{g(o),p=null}))):console.warn(`Missing element: ${e||t}`)}function g(e){e.classList.toggle("opacity-0"),e.classList.toggle("opacity-100"),e.classList.toggle("pointer-events-none"),e.classList.toggle("pointer-events-auto")}n(597),b("flyout-menu-button","flyout-menu"),b("profile-name","profile-dropdown");const v=document.getElementById("profile-name-mobile"),y=document.getElementById("mobile-profile-dropdown");v&&y&&v.addEventListener("click",(()=>y.classList.toggle("hidden")));const h=e=>{e.classList.toggle("hidden")},w=(e,t,n)=>{e.classList.add(...t),e.classList.remove(...n)},E=document.querySelector(".mobile-menu button.hamburger"),L=document.getElementById("mobile-menu");if(L){const e=L.querySelector("button"),t=L.querySelector(".fixed");if(E&&L&&e&&t){const n=()=>{h(L),h(t)};E.addEventListener("click",n),e.addEventListener("click",n),t.addEventListener("click",n);const o=document.getElementById("mobile-close-button"),i=document.getElementById("shop-mobile-button"),r=document.getElementById("mobile-return-button"),a=document.getElementById("mobile-nav"),c=document.getElementById("mobile-categories"),s=()=>{h(a),h(c),h(o),h(r)};i.addEventListener("click",(()=>s())),r.addEventListener("click",(()=>s()))}}const k=document.getElementById("women-tab"),x=document.getElementById("men-tab"),B=document.getElementById("women-tab-mobile"),I=document.getElementById("men-tab-mobile");k&&(k.addEventListener("click",(()=>S(k))),x.addEventListener("click",(()=>S(x))),B.addEventListener("click",(()=>S(B))),I.addEventListener("click",(()=>S(I))));const S=e=>{const{categories:t,tab:n,underline:o}=q("women",e.id.includes("mobile")),{categories:i,tab:r,underline:a}=q("men",e.id.includes("mobile"));"women"===e.getAttribute("category")&&t.classList.contains("hidden")?(h(t),h(i),w(n,["font-bold"],["font-semibold"]),w(r,["font-semibold"],["font-bold"]),w(o,["w-full"],[]),w(a,[],["w-full"])):"men"===e.getAttribute("category")&&i.classList.contains("hidden")&&(h(i),h(t),w(r,["font-bold"],["font-semibold"]),w(n,["font-semibold"],["font-bold"]),w(a,["w-full"],["w-0"]),w(o,["w-0"],["w-full"]))};function q(e,t=!1){const n=document.getElementById(t?"women-categories-mobile":"women-categories"),o=document.getElementById(t?"men-categories-mobile":"men-categories"),i=document.getElementById(t?"women-tab-mobile":"women-tab"),r=document.getElementById(t?"men-tab-mobile":"men-tab");return"women"===e?{categories:n,tab:i,underline:i.children[0]}:{categories:o,tab:r,underline:r.children[0]}}function O(e,t,n){e.classList.toggle("translate-x-0"),e.classList.toggle("translate-x-full"),t.classList.toggle("opacity-0"),t.classList.toggle("opacity-100"),n.classList.toggle("opacity-0"),n.classList.toggle("opacity-100")}function j(e,t,n,o,i,r){e.addEventListener("click",(()=>O(t,n,o))),i.forEach((e=>e.addEventListener("click",(()=>O(t,n,o))))),t.querySelector("ul").addEventListener("click",(e=>function(e,t,n){e.target.matches("button:not(.close-button)")&&(e.target.closest("li").remove(),function(e,t){let n=0;e.querySelectorAll("ul li").forEach((e=>{const t=e.querySelector(".price");if(t){const e=parseFloat(t.textContent.replace("$","").trim());isNaN(e)||(n+=e)}})),t.textContent=`$${n.toFixed(2)}`}(t,n))}(e,t,r)))}const A={menu:document.getElementById("shopping-cart-menu"),button:document.getElementById("shopping-cart-button"),panel:document.getElementById("sc-panel"),backdrop:document.getElementById("sc-backdrop"),closeButtons:document.getElementById("sc-panel")?.querySelectorAll(".close-button")||null,subtotalElement:document.getElementById("sc-panel")?.querySelector(".sc-subtotal")||null},M={menu:document.getElementById("shopping-cart-menu-mobile"),button:document.getElementById("shopping-cart-button-mobile"),panel:document.getElementById("sc-panel-mobile"),backdrop:document.getElementById("sc-backdrop-mobile"),closeButtons:document.getElementById("sc-panel-mobile")?.querySelectorAll(".close-button")||null,subtotalElement:document.getElementById("sc-panel-mobile")?.querySelector(".sc-subtotal")||null};null!=A.menu&&j(A.button,A.panel,A.backdrop,A.menu,A.closeButtons,A.subtotalElement),null!=M.menu&&j(M.button,M.panel,M.backdrop,M.menu,M.closeButtons,M.subtotalElement),n(924),document.querySelectorAll(".categories-toggle-btn").forEach((e=>{e&&e.addEventListener("click",(()=>{e.nextElementSibling.classList.toggle("hidden"),e.classList.toggle("text-gray-600"),e.classList.toggle("text-gray-900")}))})),document.addEventListener("DOMContentLoaded",(()=>{const e=document.getElementById("dropdown-filter");e&&e.addEventListener("change",(()=>{e.submit()}))})),b("sort-by-btn","sort-by-dropdown"),b("material-btn","material-dropdown"),b("size-btn","size-dropdown"),b("color-btn","color-dropdown"),b("brand-btn","brand-dropdown"),n(702),n(973),n(8),document.addEventListener("DOMContentLoaded",(()=>{t().init()}))})()})();