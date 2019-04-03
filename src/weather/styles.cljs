(ns weather.styles
  (:require [weather.common.ui :refer [os platform]]
            [applied-science.js-interop :as j]))

(def font-family #js {:ios #js {:font-family "AvenirNext-Regular"}
                      :android #js {:font-family "Roboto"}})

(def styles {:container {:flex 1 :justify-content "center" :align-items "center" :background-color "#fff"}
             :text-style (assoc (js->clj (j/call platform :select font-family) :keywordize-keys true) :text-align "center")
                          ; :font-family (if (= os "android") "Roboto" "AvenirNext-Regular")}
             :large-text {:font-size 44}
             :small-text {:font-size 18}
             :text-input {:background-color "#666" :color "white"
                          :height 40 :width 300
                          :margin-top 20
                          :margin-horizontal 20
                          :padding-horizontal 10
                          :align-self "center"}})
