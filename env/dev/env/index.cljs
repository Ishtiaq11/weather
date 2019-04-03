(ns env.index
  (:require [env.dev :as dev]))

;; undo main.js goog preamble hack
(set! js/window.goog js/undefined)

(-> (js/require "figwheel-bridge")
    (.withModules #js {"./assets/images/sleet.png" (js/require "../../../assets/images/sleet.png"), "./assets/images/snow.png" (js/require "../../../assets/images/snow.png"), "./assets/images/icon.png" (js/require "../../../assets/images/icon.png"), "./assets/icons/loading.png" (js/require "../../../assets/icons/loading.png"), "./assets/images/heavy-rain.png" (js/require "../../../assets/images/heavy-rain.png"), "./assets/images/heavy-cloud.png" (js/require "../../../assets/images/heavy-cloud.png"), "./assets/images/clear.png" (js/require "../../../assets/images/clear.png"), "expo" (js/require "expo"), "./assets/images/cljs.png" (js/require "../../../assets/images/cljs.png"), "./assets/icons/app.png" (js/require "../../../assets/icons/app.png"), "./assets/images/light-cloud.png" (js/require "../../../assets/images/light-cloud.png"), "react-native" (js/require "react-native"), "react" (js/require "react"), "./assets/images/light-rain.png" (js/require "../../../assets/images/light-rain.png"), "./assets/images/hail.png" (js/require "../../../assets/images/hail.png"), "./assets/images/showers.png" (js/require "../../../assets/images/showers.png"), "./assets/images/thunder.png" (js/require "../../../assets/images/thunder.png"), "create-react-class" (js/require "create-react-class"), "@expo/vector-icons" (js/require "@expo/vector-icons")}
)
    (.start "main" "expo" "192.168.0.105"))
