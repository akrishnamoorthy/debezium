function loadNotifications () {

    this.source = null;

    this.start = function () {

        var commentTable = document.getElementById("notifications");

        this.source = new EventSource("/notifications/stream");

        this.source.addEventListener("message", function (event) {

            // These events are JSON, so parsing and DOM fiddling are needed
            var notification = JSON.parse(event.data);

            var row = commentTable.getElementsByTagName("tbody")[0].insertRow(0);
            var cell0 = row.insertCell(0);
            var cell1 = row.insertCell(1);
            var cell2 = row.insertCell(2);

            cell0.className = "text";
            cell0.innerHTML = notification.id;


            cell1.className = "text";
            cell1.innerHTML = notification.old_val.firstname;

            cell2.className = "text";
            cell2.innerHTML =  notification.new_val.firstname;

        });

        this.source.onerror = function () {
            this.close();
        };

    };

    this.stop = function() {
        this.source.close();
    }

}

comment = new loadNotifications();

/*
 * Register callbacks for starting and stopping the SSE controller.
 */
window.onload = function() {
    comment.start();
};
window.onbeforeunload = function() {
    comment.stop();
}