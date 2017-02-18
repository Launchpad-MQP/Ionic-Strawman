describe('SQL factory', function() {
  var SQL;

  // Before each test load our api.users module
	beforeEach(module('ionic'));
	beforeEach(module('sql'));

  // Before each test set our injected Users factory (_Users_) to our local Users variable
  beforeEach(inject(function(_SQL_) {
		SQL = _SQL_;
  }));

  // A simple test to verify the Users factory exists
	
	it('should exist', function() {
		expect(SQL).toBeDefined();
	});
	
	// describe('.all()', function() {
		// it('should exist', function() {
			// expect(Users.all).toBeDefined();
		// });
	// });
});