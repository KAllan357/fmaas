(ns fmaas.receivers.marshaller-test
  (:require [clojure.test :refer :all]
            [fmaas.receivers.marshaller :refer :all]))

(deftest test-make-receivers
  (testing "Maps input to a serial connection and a receiver"
    (let [r (make-receivers {:COM3 {:type "PassThrough" :baud 9600 :comPort "COM3"}})]
      (is (contains? r :COM3))
      (is (contains? (r :COM3) :connection))
      (is (contains? (r :COM3) :receiver))
;      (is (= gnu.io.NRSerialPort (type (:connection (:COM3 r)))))
;      (is (instance? javax.sound.midi.Receiver (:receiver (:COM3 r))))
      )))

(deftest test-channel-to-keyword
  (testing "Converts a channel to a keyword for map access"
    (is (= :1 (channel-to-keyword 1)))
    (is (= :999 (channel-to-keyword 999)))))

(deftest test-parse-receiver-type-pass-through
  (testing "A PassThrough type yields a javax.sound.midi.Receiver interface"
    (is (instance? javax.sound.midi.Receiver (parse-receiver-type "PassThrough" "fake thing")))))

