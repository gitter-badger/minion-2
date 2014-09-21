var m = message.match(/\s*good\s+news\s*:\s*(.*)\s*/);
if (m != null) {
    minion.notify('(goodnews) ' + m[1]);
}
