package nl.sdu.elastic.specs2elastic

import java.net.{ InetAddress, ServerSocket }

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.testkit.ElasticSugar
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.node.Node
import org.elasticsearch.node.NodeBuilder._

/**
 * Helper trait for making tests talk to temporary local ElasticSearch database
 */
trait TestElastic extends ElasticSugar {

  val host = "127.0.0.1"
  val elasticPort = findPort
  val clusterName = s"elasticsearch-test-${scala.util.Random.nextLong()}"

  val node: Node = nodeBuilder().settings(newSettings.build()).local(false).node()

  val testElastic = Seq("elastic.host" -> host, "elastic.port" -> elasticPort, "elastic.cluster" -> clusterName).toMap

  override def createLocalClient = ElasticClient.fromClient(
    TransportClient.builder().settings(newSettings.build()).build().addTransportAddress(
      new InetSocketTransportAddress(InetAddress.getByName(host), elasticPort)))

  def newSettings: Settings.Builder = {
    val home = testNodeHomePath
    val conf = testNodeConfPath
    conf.toFile.mkdirs()
    conf.toFile.deleteOnExit()
    val builder = Settings.settingsBuilder()
      .put("node.http.enabled", httpEnabled)
      .put("network.host", host)
      .put("transport.tcp.port", elasticPort)
      .put("cluster.name", clusterName)
      .put("http.enabled", httpEnabled)
      .put("path.home", home.toFile.getAbsolutePath)
      .put("index.number_of_shards", numberOfShards)
      .put("index.number_of_replicas", numberOfReplicas)
      .put("script.inline", true)
      .put("script.indexed", true)
      .put("index.refresh_interval", indexRefresh.toSeconds + "s")
      .put("discovery.zen.ping.multicast.enabled", "false")
      .put("discovery.zen.ping.unicast.hosts", s"$host:$elasticPort")
      .put("es.logger.level", "DEBUG")
    configureSettings(builder)
  }

  def findPort: Int = {
    val socket: ServerSocket = new ServerSocket(0)
    val freePort = socket.getLocalPort
    socket.close()
    freePort
  }
}