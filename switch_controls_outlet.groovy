/**
 *  Switch Controls Outlet
 *
 *  Author: brian@bevey.org
 *  Date: 2013-11-16
 */
preferences {
  section("Turn on with which switch?") {
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
  init()
}

def updated() {
  unsubscribe()
  init()
}

def init() {
  subscribe(wallSwitch, "switch", lightSwitch)

  if(offToggle == "Yes") {
    subscribe(wallSwitch, "switch.off", doubleSwitch, [filterEvents: false])
  }
}

def changeLights(state) {
  if(state == "off") {
    outlets?.off();
  }

  else {
    outlets?.on();
  }
}

def lightSwitch(evt) {
  if(evt.value == "on") {
    log.info("Turning on lights")

    changeLights("on")
  }

  else {
    log.info("Turning off lights")

    changeLights("off")
  }
}

def doubleSwitch(evt) {
  log.info("Switch is off, but we want to toggle outlets")

  if(outlets.findAll { it?.latestValue("switch") == "on" }) {
    log.info("Toggle lights off")

    changeLights("off")
  }

  else {
    log.info("Toggle lights on")

    changeLights("on")
  }
}