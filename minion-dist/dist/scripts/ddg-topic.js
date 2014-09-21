var m = message.match(/\?\s*(.*)\s*/);

var search;
var summarize = function(url) {
    var json = minion.json(url);
    var summary = JSON.parse(json);
    var abstract = summary.AbstractText;
    if (abstract) {
        return abstract;
    }
    var relatedTopic = summary.RelatedTopics[0];
    if (relatedTopic != null) {
        return summarize(relatedTopic.FirstURL + '?format=json');
    }
    return 'https://duckduckgo.com/?q=' + search;
};

if (m != null) {
    search = encodeURIComponent(m[1]);
    minion.reply('(ddg) ' + summarize('https://api.duckduckgo.com/?format=json&q=' + search));
}
