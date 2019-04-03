(ns weather.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :get-greeting
 (fn [db _]
   (:greeting db)))

(reg-sub
  :city-name
  (fn [db _]
    (:city-name db)))

(reg-sub
  :app-db
  (fn [db _]
    db))
