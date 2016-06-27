(ns fmaas.config.config-test
  (:require [clojure.test :refer :all]
            [fmaas.config.config :refer :all]))

(deftest config-test
  (testing "Config is loaded"
    (is (not (nil? get-config))))
  (testing "Config has default data"
    (is (= 8080 (:port get-config)))
    (is (= java.io.File (type (:songs-dir get-config))))))
