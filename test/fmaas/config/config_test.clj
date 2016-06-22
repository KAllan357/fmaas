(ns fmaas.config.config-test
  (:require [clojure.test :refer :all]
            [fmaas.config.config :refer :all]))

(defn override-config [env]
  (nomad/with-location-override {:environment env} (my-config)))

(deftest config-test
  (testing "Config is loaded"
    (is (not (nil? my-config))))
  (testing "Config has data for development"
    (is (= "buzz" (:fizz (override-config "dev")))))
  (testing "Config has data for production"
    (is (= "bizz" (:fuzz (override-config "prod"))))))
