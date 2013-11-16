/**
 *  Switch Controls Outlet
 *
 *  Author: brian@bevey.org
 *  Date: 2013-11-16
 */
preferences {
  section("Turn on a which switch?") {
    input "wallSwitch", "capability.switch"
  }

  section("Turns on which outlets?") {
    input "outlets", "capability.switch", multiple: true
  }

  section("If switch is off, use off as toggle for outlets?") {
    input "offToggle", "enum", metadata: [values: ["Yes", "No"]], required: false
  }
}

def installed() {
  state.light = "on"
  subscribe(wallSwitch, "switch", changeLights, [filterEvents: false])
}

def updated() {
  state.light = "on"
  unsubscribe()
  subscribe(wallSwitch, "switch", changeLights, [filterEvents: false])
}

def changeLights(evt) {
  if (evt.value == "off" && wallSwitch.latestValue("switch") == "off" && offToggle == "Yes") {
    log.info("Switch is off, but we want to toggle outlets")

    if(outlets.findAll { it?.latestValue("switch") == "on" }) {
      log.info("Toggle lights off")

      outlets?.off()
    }

    else {
      log.info("Toggle lights on")

      outlets?.on()
    }
  }

  else if(evt.value == "on") {
    log.info("Turning on lights")

    outlets?.on()
  }

  else {
    log.info("Turning off lights")

    outlets?.off()
  }
}