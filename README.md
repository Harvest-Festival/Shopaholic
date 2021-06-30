<img src="https://harvestfestivalwiki.com/images/f/f9/Shopaholic-Logo.svg" width="70%">

[![Discord](https://img.shields.io/discord/227497118498029569?style=plastic&colorB=7289DA&logo=discord&logoColor=white)](http://discord.gg/0vVjLvWg5kyQwnHG) &nbsp; ![GitHub](https://img.shields.io/github/license/Harvest-Festival/Shopaholic?color=%23990000&style=plastic)

Shopaholic is the economy part of Harvest Festival, with shops, shipping and gold. It will function as a utility that can be used to add shops to any block, item or entity. And to add prices to any items to be sold via shipping. With a multitude of conditions and item types. At present it will only function as something a modpack author/server owner can edit, but there are plans to expand that further down the line.

Adding Shopaholic to your buildscript
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    url 'https://maven.joshiejack.uk/'
  }
}

dependencies {
    compile fg.deobf("uk.joshiejack.penguinlib:Penguin-Lib:${minecraft_version}-${penguinlib_version}")
    compile fg.deobf("uk.joshiejack.penguinlib:Penguin-Lib:${minecraft_version}-${shopaholic_version}")
}
```

`${$penguinlib_version}` can be found [here](https://maven.joshiejack.uk/uk/joshiejack/penguinlib/Penguin-Lib/)
`${shopaholic_version}` can be found [here](https://maven.joshiejack.uk/uk/joshiejack/penguinlib/Shopaholic/)