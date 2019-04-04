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

(reg-sub
  :weather-state-name
  (fn [db _]
    (:weather-state-name db)))

(reg-sub
  :temperature
  (fn [db _]
    (:temperature db)))

(reg-sub
  :loading
  (fn [db _]
    (:loading db)))

(reg-sub
  :error
  (fn [db _]
    (:error db)))
