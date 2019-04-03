(ns weather.common.ui
  (:require [applied-science.js-interop :as j]
            [reagent.core :as r :refer [atom]]))

(def ReactNative (js/require "react-native"))
(def expo (js/require "expo"))
(def AtExpo (js/require "@expo/vector-icons"))
(def ionicons (j/get AtExpo :Ionicons))
(def ic (r/adapt-react-class ionicons))

(def text (r/adapt-react-class (j/get ReactNative :Text)))
(def text-input (r/adapt-react-class (j/get ReactNative :TextInput)))
(def view (r/adapt-react-class (j/get ReactNative :View)))
(def keyboard-avoiding-view (r/adapt-react-class (j/get ReactNative :KeyboardAvoidingView)))
(def image (r/adapt-react-class (j/get ReactNative :Image)))
(def img-bg (r/adapt-react-class (j/get ReactNative :ImageBackground)))
(def touchable-highlight (r/adapt-react-class (j/get ReactNative :TouchableHighlight)))
(def platform (j/get ReactNative :Platform))
(def os (j/get platform :OS))
(def Alert (j/get ReactNative :Alert))

(defn alert [title]
  (j/call Alert :alert  title))
